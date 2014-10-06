package fraud.logic.velocity

import common.exceptions.ServiceException
import fraud.db.dao.IVelocityDAO
import fraud.db.dao.redis.IRedisVelocityDAO
import fraud.exceptions.FraudServiceEventLogId
import fraud.logic.velocity.concurrency.RelatedMetricsComputationTask
import fraud.rest.v1.velocity.*
import fraud.rest.v1.velocity.redis.Aggregation
import fraud.rest.v1.velocity.redis.VMetric
import fraud.util.DBCache
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.ExecutionException
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.Future
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
    DBCache dbCache

    private static final ForkJoinPool FJP = new ForkJoinPool(32)

    @Override
    List<VelocityMetric> checkFJP(final Map<String, String> metrics) {
        List<VelocityMetric> resultMetrics = []
        List<VelocityData> velocityDataToSave = []

        // getting velocity configuration from cache
        final Map<String, VelocityConfig> configs = dbCache.velocityConfigs

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
            velocityDAO.saveBatchOfMetrics(resultMetrics)
        }
        if (velocityDataToSave) {
            velocityDAO.saveBatchOfData(velocityDataToSave)
        }

        // return
        resultMetrics
    }

    @Override
    List<VelocityMetric> checkCassandraAsync(final Map<String, String> metrics) {
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
                final Map<String, VelocityConfig> configs = dbCache.velocityConfigs

                List<RelatedMetricsComputationTask> velocityDataUpdateTasks = []
                metrics.each { key, value ->
                    final VelocityConfig config = configs[(key)]
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
            }
        })
        result
    }

    @Override
    List<VMetric> checkRedisSync(Map<String, String> velocityRQ) {
        final def velocityMetrics = []
        withPool { jsr166y.ForkJoinPool pool ->
            velocityRQ.eachParallel { final metricType, final metricValue ->
                final def newMetric = new VMetric(type: metricType, value: metricValue, metrics: [:])
                withExistingPool(pool) {
                    velocityRQ.eachParallel { final secondaryMetricType, final secondaryMetricValue ->
                        if (metricType != secondaryMetricType) {
                            redisVelocityDAO.logData("$metricType:$metricValue:$secondaryMetricType", secondaryMetricValue)
                        }
                    }
                    dbCache.velocityConfigs[(metricType)]?.metricsAggregationConfig?.eachParallel {
                        relatedMetricType, aggregations ->
                            newMetric.metrics << [(relatedMetricType): [:]]
                            aggregations?.each { def conf ->
                                String key = "$metricType:$metricValue:$relatedMetricType"
                                List<String> history = redisVelocityDAO.getHistoricalData(key, conf.period as Long)
                                def aggregation = Aggregation.valueOf(conf.type.toString())
                                Double aggregationResult = aggregation.apply history
                                redisVelocityDAO.saveMetric(key, aggregation, aggregationResult)
                                newMetric.metrics[(relatedMetricType)] << [(aggregation): aggregationResult]
                            }
                    }
                    velocityMetrics << newMetric
                }
            }
        }
        return velocityMetrics
    }

    @Override
    List<VMetric> checkRedisAsync(Map<String, String> velocityRQ) {
        def velocityMetrics = []
        velocityRQ.each { metricType, metricValue ->
            def velocity = new VMetric(type: metricType, value: metricValue, metrics: [:])
            dbCache.velocityConfigs[(metricType)]?.metricsAggregationConfig?.each { relatedMetricType, aggregations ->
                velocity.metrics <<
                        [(relatedMetricType): redisVelocityDAO.getMetrics("$metricType:$metricValue:$relatedMetricType")]
            }
            velocityMetrics << velocity
        }
        FJP.submit(new RecursiveAction() {
            @Override
            protected void compute() {
                checkRedisSync(velocityRQ)
            }
        })
        return velocityMetrics
    }

    @Override
    List<VelocityMetric> checkGPars(final Map<String, String> metrics) {
        List<VelocityMetric> resultMetrics = []
        final Map<String, VelocityConfig> configs = dbCache.velocityConfigs

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
                                velocityDAO.saveData(newData)
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
                                velocityDAO.saveBatchOfMetrics(newMetrics)
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
        final Map<String, VelocityConfig> configs = dbCache.velocityConfigs

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
