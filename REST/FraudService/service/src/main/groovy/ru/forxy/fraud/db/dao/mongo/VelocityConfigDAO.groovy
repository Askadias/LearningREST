package ru.forxy.fraud.db.dao.mongo

import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.exception.ExceptionUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import ru.forxy.common.status.pojo.ComponentStatus
import ru.forxy.common.status.pojo.StatusType
import ru.forxy.fraud.db.dao.IVelocityConfigDAO
import ru.forxy.fraud.rest.v1.velocity.VelocityConfig

/**
 * Mongo DB based data source for velocity configurations
 */
class VelocityConfigDAO implements IVelocityConfigDAO {

    private MongoTemplate mongoTemplate

    @Override
    Iterable<VelocityConfig> findAll(final Sort sort) {
        mongoTemplate.find(Query.query(new Criteria()).with(sort), VelocityConfig.class)
    }

    @Override
    Page<VelocityConfig> findAll(final Pageable pageable) {
        new PageImpl<>(
                mongoTemplate.find(Query.query(new Criteria()).with(pageable), VelocityConfig.class),
                pageable, count()
        )
    }

    @Override
    Page<VelocityConfig> findAll(final Pageable pageable, final VelocityConfig filter) {
        Query query = Query.query(new Criteria()).with(pageable)
        if (filter != null) {
            if (StringUtils.isNotEmpty(filter.getMetricType())) {
                query.addCriteria(new Criteria('metricType').regex(filter.getMetricType(), 'i'))
            }
            if (StringUtils.isNotEmpty(filter.getUpdatedBy())) {
                query.addCriteria(new Criteria('updatedBy').regex(filter.getUpdatedBy(), 'i'))
            }
            if (StringUtils.isNotEmpty(filter.getCreatedBy())) {
                query.addCriteria(new Criteria('createdBy').regex(filter.getCreatedBy(), 'i'))
            }
        }

        new PageImpl<>(mongoTemplate.find(query, VelocityConfig.class), pageable, count())
    }

    @Override
    <S extends VelocityConfig> S save(final S velocityConfig) {
        mongoTemplate.save(velocityConfig)
        velocityConfig
    }

    @Override
    <S extends VelocityConfig> Iterable<S> save(final Iterable<S> velocityConfigs) {
        throw null
    }

    @Override
    VelocityConfig findOne(final String metricType) {
        mongoTemplate.findOne(Query.query(Criteria.where('metricType').is(metricType)), VelocityConfig.class)
    }

    @Override
    boolean exists(final String metricType) {
        mongoTemplate.findOne(Query.query(Criteria.where('metricType').is(metricType)), VelocityConfig.class) != null
    }

    @Override
    Iterable<VelocityConfig> findAll() {
        mongoTemplate.findAll(VelocityConfig.class)
    }

    @Override
    Iterable<VelocityConfig> findAll(final Iterable<String> metricTypes) {
        mongoTemplate.find(Query.query(Criteria.where('metricType').in(metricTypes)), VelocityConfig.class)
    }

    @Override
    long count() {
        mongoTemplate.count(null, VelocityConfig.class)
    }

    @Override
    void delete(final String metricType) {
        mongoTemplate.remove(Query.query(Criteria.where('metricType').is(metricType)), VelocityConfig.class)
    }

    @Override
    void delete(final VelocityConfig velocityConfig) {
        mongoTemplate.remove(velocityConfig)
    }

    @Override
    void delete(final Iterable<? extends VelocityConfig> velocityConfigs) {
        for (VelocityConfig velocityConfig : velocityConfigs) {
            mongoTemplate.remove(velocityConfig)
        }
    }

    @Override
    void deleteAll() {
        mongoTemplate.remove(null, VelocityConfig.class)
    }

    void setMongoTemplate(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate
    }

    @Override
    ComponentStatus getStatus() {
        String location = null
        StatusType statusType = StatusType.GREEN
        long responseTime = Long.MAX_VALUE
        String exceptionMessage = null
        String exceptionDetails = null
        if (mongoTemplate != null && mongoTemplate.getDb() != null && mongoTemplate.getDb().getMongo() != null) {
            location = mongoTemplate.getDb().getMongo().getConnectPoint()

            long timeStart = new Date().getTime()
            try {
                mongoTemplate.count(null, VelocityConfig.class)
            } catch (final Exception e) {
                exceptionMessage = e.getMessage()
                exceptionDetails = ExceptionUtils.getStackTrace(e)
                statusType = StatusType.RED
            } finally {
                responseTime = new Date().getTime() - timeStart
            }


        } else {
            statusType = StatusType.RED
        }
        new ComponentStatus('VelocityConfig DAO', location, statusType, null, ComponentStatus.ComponentType.DB,
                responseTime, null, exceptionMessage, exceptionDetails)
    }

    Object save(Object o){return o}
}
