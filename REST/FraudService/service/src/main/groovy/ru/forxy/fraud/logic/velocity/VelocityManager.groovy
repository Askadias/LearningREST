package ru.forxy.fraud.logic.velocity

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.forxy.common.exceptions.ServiceException
import ru.forxy.fraud.db.dao.IVelocityDAO
import ru.forxy.fraud.exceptions.FraudServiceEventLogId
import ru.forxy.fraud.logic.velocity.concurrency.RelatedMetricsComputationTask
import ru.forxy.fraud.rest.v1.velocity.*
import ru.forxy.fraud.util.OperationalDataStorage

import java.util.concurrent.ExecutionException
import java.util.concurrent.ForkJoinPool

import static groovyx.gpars.GParsPool.withExistingPool
import static groovyx.gpars.GParsPool.withPool

/**
 * Implementation class for BlackListService business logic
 */
class VelocityManager implements IVelocityManager {

    private static final int DEFAULT_PAGE_SIZE = 30

    private static final Logger LOGGER = LoggerFactory.getLogger(VelocityManager.class)

    IVelocityDAO velocityDAO
    OperationalDataStorage operationalDataStorage

    private static final ForkJoinPool FJP = new ForkJoinPool(32)

    @Override
    List<VelocityMetric> check(final Map<String, String> metrics) {
        List<VelocityMetric> resultMetrics = []
        List<VelocityData> velocityDataToSave = []

        // getting velocity configuration from cache
        final Map<String, VelocityConfig> configs = operationalDataStorage.configsByMetricType

        List<RelatedMetricsComputationTask> velocityDataUpdateTasks = []
        metrics.each { key, value ->
            final VelocityConfig config = configs[key]
            // if incoming metric has configuration then retrieve it's related metrics configuration
            if (config != null) {
                RelatedMetricsComputationTask dataUpdateTask = new RelatedMetricsComputationTask(
                        config: config,
                        metricType: key,
                        metricValue: value,
                        metrics: metrics,
                        velocityDAO: velocityDAO)
                velocityDataUpdateTasks.add(dataUpdateTask)
                FJP.submit(dataUpdateTask)
            }
        }
        velocityDataUpdateTasks.each {
            try {
                it.get().each { VelocityPartitionKey key, Map<VelocityData, List<VelocityMetric>> resultsMap ->
                    resultsMap.each { VelocityData data, List<VelocityMetric> metricList ->
                        velocityDataToSave << data
                        resultMetrics += metricList
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                new ServiceException(e,
                        FraudServiceEventLogId.UnexpectedErrorDuringVelocityComputation,
                        it.metricType).log(LOGGER)
            }
        }

        if (resultMetrics) {
            velocityDAO.saveBatchOfMetrics(resultMetrics)
        }
        if (velocityDataToSave) {
            velocityDAO.saveBatchOfData(velocityDataToSave)
        }

        // return
        resultMetrics
    }

    List<VelocityMetric> checkGPars(final Map<String, String> metrics) {
        List<VelocityMetric> resultMetrics = []
        List<VelocityData> velocityDataToSave = []

        withPool { jsr166y.ForkJoinPool pool ->
            Map<String, Map<VelocityData, List<VelocityMetric>>> result =
                    metrics.collectParallel { metricType, metricValue ->
                        withExistingPool(pool) {
                            def id = new VelocityPartitionKey(metricType: metricType, metricValue: metricValue)
                            final Map<String, VelocityConfig> configs = operationalDataStorage.configsByMetricType
                            Map<VelocityData, List<VelocityMetric>> dataListMap =
                                    configs[metricType]?.metricsAggregationConfig?.collectParallel {
                                        relatedMetricType, aggregations ->
                                            withExistingPool(pool) {

                                                def newData = new VelocityData(
                                                        key: new VelocityDataCompositeKey(
                                                                id: id,
                                                                relatedMetricType: relatedMetricType,
                                                                createDate: new Date()),
                                                        relatedMetricValue: metrics[relatedMetricType])

                                                List<VelocityMetric> dataMetrics = aggregations.collectParallel {
                                                    List<VelocityData> data = velocityDAO
                                                            .getMetricDataForPeriod(id, relatedMetricType, it.period) + newData;
                                                    //data << newData
                                                    return new VelocityMetric(
                                                            key: new VelocityMetricCompositeKey(
                                                                    id: id,
                                                                    relatedMetricType: relatedMetricType,
                                                                    aggregationType: it.type
                                                            ),
                                                            aggregatedValue: it.type.apply(data)
                                                    )
                                                }
                                                return [(newData): dataMetrics]
                                            }
                                    }?.parallelArray?.reduce { a, b ->
                                        b.keySet().each {
                                            a[it] ? a[it].addAll(b[it]) : a[it].put(b[it])
                                        }
                                    }
                            return [(metricType): dataListMap]
                        }
                    }?.parallelArray?.reduce { a, b ->
                        b.keySet().each {
                            a[it] ? a[it].addAll(b[it]) : a[it].put(b[it])
                        }
                    }
            resultMetrics = result?.values()?.collect { it?.values() }
            velocityDataToSave = result?.values()?.collect { it?.keySet() }
            if (resultMetrics) {
                velocityDAO.saveBatchOfMetrics(resultMetrics)
            }
            if (velocityDataToSave) {
                velocityDAO.saveBatchOfData(velocityDataToSave)
            }

        }

        return resultMetrics
    }

    @Override
    List<VelocityMetric> check2(final Map<String, String> metrics) {
        return checkGPars(metrics);
        List<VelocityMetric> resultMetrics = []
        List<VelocityData> velocityDataToSave = []

        // getting velocity configuration from cache
        final Map<String, VelocityConfig> configs = operationalDataStorage.configsByMetricType

        metrics.each { metricType, metricValue ->
            def id = new VelocityPartitionKey(metricType: metricType, metricValue: metricValue)
            configs[metricType]?.metricsAggregationConfig?.each { relatedMetricType, aggregation ->
                aggregation.each {
                    List<VelocityData> data = velocityDAO.getMetricDataForPeriod(id, relatedMetricType, it.period);

                    def newData = new VelocityData(
                            key: new VelocityDataCompositeKey(
                                    id: id,
                                    relatedMetricType: relatedMetricType,
                                    createDate: new Date()),
                            relatedMetricValue: metrics[relatedMetricType])

                    velocityDataToSave << newData
                    data << newData

                    resultMetrics << new VelocityMetric(
                            key: new VelocityMetricCompositeKey(
                                    id: id,
                                    relatedMetricType: relatedMetricType,
                                    aggregationType: it.type
                            ),
                            aggregatedValue: it.type.apply(data)
                    )
                }
            }
        }
        if (resultMetrics) {
            velocityDAO.saveBatchOfMetrics(resultMetrics)
        }
        if (velocityDataToSave) {
            velocityDAO.saveBatchOfData(velocityDataToSave)
        }

        return resultMetrics
    }

    @Override
    List<VelocityMetric> getMoreMetricsFrom(final String metricType, final String value) {
        getMoreMetricsFrom(metricType, value, DEFAULT_PAGE_SIZE)
    }

    @Override
    List<VelocityMetric> getMoreMetricsFrom(final String metricType, final String value, final int limit) {
        velocityDAO.getMoreMetrics(metricType, value, limit)
    }

    @Override
    List<VelocityMetric> getMetrics(final String metricType, final String metricValue) {
        velocityDAO.getMetrics(new VelocityPartitionKey(metricType: metricType, metricValue: metricValue))
    }

    @Override
    VelocityMetric getMetric(final String metricType, final String metricValue, final String relatedMetricType,
                             final AggregationType aggregationType) {
        velocityDAO.getMetric(new VelocityMetricCompositeKey(
                id: new VelocityPartitionKey(
                        metricType: metricType,
                        metricValue: metricValue),
                relatedMetricType: relatedMetricType,
                aggregationType: aggregationType))
    }

    @Override
    List<VelocityData> getMoreDataFrom(final String metricType, final String value,
                                       final String relatedMetricType, final Date createDate) {
        getMoreDataFrom(metricType, value, relatedMetricType, createDate, DEFAULT_PAGE_SIZE)
    }

    @Override
    List<VelocityData> getMoreDataFrom(final String metricType, final String value,
                                       final String relatedMetricType, final Date createDate, final int limit) {
        velocityDAO.getMoreData(metricType, value, relatedMetricType, createDate, limit)
    }

    @Override
    List<VelocityData> getDataList(final String metricType, final String metricValue) {
        velocityDAO.getDataList(new VelocityPartitionKey(metricType: metricType, metricValue: metricValue))
    }

    @Override
    VelocityData getData(final String metricType, final String metricValue, final String relatedMetricType,
                         final Date createDate) {
        velocityDAO.getData(new VelocityDataCompositeKey(
                id: new VelocityPartitionKey(
                        metricType: metricType,
                        metricValue: metricValue),
                relatedMetricType: relatedMetricType,
                createDate: createDate))
    }

    @Override
    void addMetric(final VelocityMetric metric) {
        velocityDAO.saveMetric(metric)
    }

    @Override
    void addData(final VelocityData data) {
        velocityDAO.saveData(data)
    }

    @Override
    void updateMetric(final VelocityMetric metric) {
        velocityDAO.saveMetric(metric)
    }

    @Override
    void updateData(final VelocityData data) {
        velocityDAO.saveData(data)
    }
}
