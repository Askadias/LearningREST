package ru.forxy.fraud.logic.velocity

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.forxy.common.exceptions.ServiceException
import ru.forxy.fraud.db.dao.IVelocityDAO
import ru.forxy.fraud.db.dao.redis.IRedisVelocityDAO
import ru.forxy.fraud.exceptions.FraudServiceEventLogId
import ru.forxy.fraud.logic.velocity.concurrency.RelatedMetricsComputationTask
import ru.forxy.fraud.rest.v1.velocity.*
import ru.forxy.fraud.rest.v1.velocity.redis.Aggregation
import ru.forxy.fraud.rest.v1.velocity.redis.VKey
import ru.forxy.fraud.rest.v1.velocity.redis.VMetric
import ru.forxy.fraud.util.OperationalDataStorage

import java.util.concurrent.ExecutionException
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveAction

import static groovyx.gpars.GParsPool.withExistingPool
import static groovyx.gpars.GParsPool.withPool

/**
 * Implementation class for BlackListService business logic
 */
class VelocityManager implements IVelocityManager {

    private static final int DEFAULT_PAGE_SIZE = 30

    private static final Logger LOGGER = LoggerFactory.getLogger(VelocityManager.class)

    IVelocityDAO velocityDAO
    IRedisVelocityDAO redisVelocityDAO;
    OperationalDataStorage operationalDataStorage

    private static final ForkJoinPool FJP = new ForkJoinPool(32)

    @Override
    List<VelocityMetric> checkFJP(final Map<String, String> metrics) {
        List<VelocityMetric> resultMetrics = []
        List<VelocityData> velocityDataToSave = []

        // getting velocity configuration from cache
        final Map<String, VelocityConfig> configs = operationalDataStorage.configsByMetricType

        List<RelatedMetricsComputationTask> velocityDataUpdateTasks = []
        metrics.each { key, value ->
            final List<VelocityConfig> config = configs[key] as List<VelocityConfig>
            // if incoming metric has configuration then retrieve it's related metrics configuration
            if (config) {
                config.each {
                    RelatedMetricsComputationTask dataUpdateTask = new RelatedMetricsComputationTask(
                            config: it,
                            metricType: key,
                            metricValue: value,
                            metrics: metrics,
                            velocityDAO: velocityDAO)
                    velocityDataUpdateTasks.add(dataUpdateTask)
                    FJP.submit(dataUpdateTask)
                }
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
            //velocityDAO.saveBatchOfMetrics(resultMetrics)
        }
        if (velocityDataToSave) {
            //velocityDAO.saveBatchOfData(velocityDataToSave)
        }

        // return
        resultMetrics
    }

    @Override
    List<VelocityMetric> checkAsync(final Map<String, String> metrics) {
        List<VelocityMetric> result = []
        metrics.each { metricType, metricValue ->
            result += velocityDAO.getMetrics(new VelocityPartitionKey(
                    metricType: metricType,
                    metricValue: metricValue
            ))
        }
        FJP.submit(new RecursiveAction() {
            @Override
            protected void compute() {
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
                    //velocityDAO.saveBatchOfMetrics(resultMetrics)
                }
                if (velocityDataToSave) {
                    //velocityDAO.saveBatchOfData(velocityDataToSave)
                }
            }
        })
        result
    }

    @Override
    List<VMetric> checkRedisGParsAsync(final Map<String, String> metrics) {
        List<VMetric> resultMetrics = []
        final Map<String, VelocityConfig> configs = operationalDataStorage.configsByMetricType

        withPool { jsr166y.ForkJoinPool pool ->
            metrics.each { metricType, metricValue ->
                VMetric newMetric = new VMetric(
                        metricType: metricType,
                        metricValue: metricValue,
                        aggregations: new HashMap<String, Map<Aggregation, Double>>()
                )
                configs[metricType]?.metricsAggregationConfig?.each {
                    it.each { relatedMetricType, aggregations ->
                        newMetric.aggregations << [(relatedMetricType): redisVelocityDAO.getMetrics(new VKey(
                                metricType: metricType,
                                metricValue: metricValue,
                                relatedMetricType: relatedMetricType
                        ))]
                    }
                    /*it.collectParallel { relatedMetricType, aggregations ->
                        withExistingPool(pool) {
                            final VKey key = new VKey(
                                    metricType: metricType,
                                    metricValue: metricValue,
                                    relatedMetricType: relatedMetricType
                            )
                            String newData = metrics[relatedMetricType] ?: null;
                            def metricsAggregation = aggregations.collectParallel {
                                List<String> data = redisVelocityDAO.getHistoricalData(key, it.period)
                                if (newData) data += newData
                                Aggregation aggregation = Aggregation.valueOf(it.type.toString());
                                return [aggregation, aggregation.apply(data)]
                            }
                            redisVelocityDAO.logData(key, newData);
                            [relatedMetricType, metricsAggregation]
                        }
                    }?.each {
                        String relatedMetricType = it[0] as String
                        newMetric.aggregations << [(relatedMetricType): new HashMap<Aggregation, Double>()]
                        it[1].each {
                            newMetric.aggregations[(relatedMetricType)] << [(it[0]): it[1]]
                            redisVelocityDAO.saveMetric(new VKey(
                                    metricType: newMetric.metricType,
                                    metricValue: newMetric.metricValue,
                                    relatedMetricType: relatedMetricType
                            ), it[0], it[1])
                        }
                    }*/
                }
                resultMetrics << newMetric
            }
        }

        return resultMetrics
    }

    @Override
    List<VMetric> checkRedisGPars(final Map<String, String> metrics) {
        List<VMetric> resultMetrics = []
        final Map<String, VelocityConfig> configs = operationalDataStorage.configsByMetricType

        withPool { jsr166y.ForkJoinPool pool ->
            metrics.each { metricType, metricValue ->
                VMetric newMetric = new VMetric(
                        metricType: metricType,
                        metricValue: metricValue,
                        aggregations: new HashMap<String, Map<Aggregation, Double>>()
                )
                configs[metricType]?.metricsAggregationConfig?.each {
                    it.collectParallel { relatedMetricType, aggregations ->
                        withExistingPool(pool) {
                            final VKey key = new VKey(
                                    metricType: metricType,
                                    metricValue: metricValue,
                                    relatedMetricType: relatedMetricType
                            )
                            String newData = metrics[relatedMetricType] ?: null;
                            def metricsAggregation = aggregations.collectParallel {
                                List<String> data = redisVelocityDAO.getHistoricalData(key, it.period)
                                if (newData) data += newData
                                Aggregation aggregation = Aggregation.valueOf(it.type.toString());
                                return [aggregation, aggregation.apply(data)]
                            }
                            redisVelocityDAO.logData(key, newData);
                            [relatedMetricType, metricsAggregation]
                        }
                    }?.each {
                        String relatedMetricType = it[0] as String
                        newMetric.aggregations << [(relatedMetricType): new HashMap<Aggregation, Double>()]
                        it[1].each {
                            newMetric.aggregations[(relatedMetricType)] << [(it[0]): it[1]]
                            redisVelocityDAO.saveMetric(new VKey(
                                    metricType: newMetric.metricType,
                                    metricValue: newMetric.metricValue,
                                    relatedMetricType: relatedMetricType
                            ), it[0], it[1])
                        }
                    }
                }
                resultMetrics << newMetric
            }
        }

        return resultMetrics
    }

    @Override
    List<VelocityMetric> checkGPars(final Map<String, String> metrics) {
        List<VelocityMetric> resultMetrics = []
        final Map<String, VelocityConfig> configs = operationalDataStorage.configsByMetricType

        withPool { jsr166y.ForkJoinPool pool ->
            metrics.each { metricType, metricValue ->
                final def id = new VelocityPartitionKey(metricType: metricType, metricValue: metricValue)
                configs[metricType]?.metricsAggregationConfig?.each {
                    it.collectParallel { relatedMetricType, aggregations ->
                        withExistingPool(pool) {

                            VelocityData newData = null;
                            if (metrics[relatedMetricType]) {
                                newData = new VelocityData(
                                        key: new VelocityDataCompositeKey(
                                                id: id,
                                                relatedMetricType: relatedMetricType,
                                                createDate: new Date()),
                                        relatedMetricValue: metrics[relatedMetricType])
                                //velocityDAO.saveData(newData)
                            }

                            return aggregations.collectParallel {
                                def data = velocityDAO.getMetricDataForPeriod(id, relatedMetricType, it.period)
                                if (newData) data += newData

                                List<VelocityMetric> newMetrics = new VelocityMetric(
                                        key: new VelocityMetricCompositeKey(
                                                id: id,
                                                relatedMetricType: relatedMetricType,
                                                aggregationType: it.type
                                        ),
                                        aggregatedValue: it.type.apply(data)
                                ) as List<VelocityMetric>
                                //velocityDAO.saveBatchOfMetrics(newMetrics)
                                return newMetrics
                            }
                        }
                    }?.each {
                        resultMetrics += it
                    }
                }
            }
        }

        return resultMetrics
    }


    @Override
    List<VelocityMetric> checkSync(final Map<String, String> metrics) {
        List<VelocityMetric> resultMetrics = []
        List<VelocityData> velocityDataToSave = []

        // getting velocity configuration from cache
        final Map<String, VelocityConfig> configs = operationalDataStorage.configsByMetricType

        metrics.each { metricType, metricValue ->
            def id = new VelocityPartitionKey(metricType: metricType, metricValue: metricValue)
            configs[metricType]?.metricsAggregationConfig?.each {
                it?.each { relatedMetricType, aggregation ->
                    aggregation.each {
                        List<VelocityData> data = velocityDAO.getMetricDataForPeriod(id, relatedMetricType, it.period);

                        if (metrics[relatedMetricType]) {
                            def newData = new VelocityData(
                                    key: new VelocityDataCompositeKey(
                                            id: id,
                                            relatedMetricType: relatedMetricType,
                                            createDate: new Date()),
                                    relatedMetricValue: metrics[relatedMetricType])

                            velocityDataToSave << newData
                            data << newData
                        }

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
        }
        if (resultMetrics) {
            //velocityDAO.saveBatchOfMetrics(resultMetrics)
        }
        if (velocityDataToSave) {
            //velocityDAO.saveBatchOfData(velocityDataToSave)
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
