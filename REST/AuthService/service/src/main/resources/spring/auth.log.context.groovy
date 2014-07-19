package spring

import ru.forxy.common.logging.AbstractPerformanceLogger
import ru.forxy.common.logging.LoggingInterceptor
import ru.forxy.common.logging.LoggingServletFilter
import ru.forxy.common.logging.exceptions.DatabaseExceptionHandler
import ru.forxy.common.logging.exceptions.ServiceExceptionHandler
import ru.forxy.common.logging.extractor.SpringELFieldExtractor
import ru.forxy.common.logging.support.Fields
import ru.forxy.common.logging.writer.LogWriter
import ru.forxy.common.support.Configuration

beans {

    performanceConfiguration(Configuration) {
        settings = [(LoggingServletFilter.Configs.IsHttpInfoLoggingEnabled)        : 'true',
                    (AbstractPerformanceLogger.Configs.IsPayloadLoggingEnabled)    : 'true',
                    (AbstractPerformanceLogger.Configs.IsPerformanceLoggingEnabled): 'true']
    }

    endpointPerformanceConfiguration(Configuration) { bean ->
        bean.parent = performanceConfiguration
        settings = [(LoggingServletFilter.Configs.IsHttpInfoLoggingEnabled)        : 'true',
                    (AbstractPerformanceLogger.Configs.IsPayloadLoggingEnabled)    : 'true',
                    (AbstractPerformanceLogger.Configs.IsPerformanceLoggingEnabled): 'true',
                    (Fields.ActivityName)                                          : 'endpoint']
    }

    // ================= SERVICE =================================================================================

    requestLogger(LogWriter, 'auth.performance.request')
    responseLogger(LogWriter, 'auth.performance.response')

    endpointPerflogFilter(LoggingServletFilter) {
        configuration = ref(endpointPerformanceConfiguration)
        requestWriter = ref(requestLogger)
        responseWriter = ref(responseLogger)
        requestFieldExtractors = { SpringELFieldExtractor ex ->
            extractRules = ['Authname'       : '#request.getParameter(\'client_id\')?:#requestHeaders[\'client-id\']?:null',
                            'TransactionGUID': '#request.getParameter(\'transaction_guid\')?:#requestHeaders[\'transaction-guid\']?:null',
                            'MessageGUID'    : '#request.getParameter(\'message_guid\')?:#requestHeaders[\'message-guid\']?:null',
                            'OperationName'  : '#request.requestURI.matches(\'.*/auths[/\\d+]+\') and #request.getMethod().equals(\'GET\') ? \'GetAuthsPage\' :' +
                                    '#request.requestURI.matches(\'.*/auths/login/?\') and #request.getMethod().equals(\'POST\') ? \'Login\' :' +
                                    '#request.requestURI.matches(\'.*/auths/?\') and #request.getMethod().equals(\'GET\') ? \'GetAuth\' :' +
                                    '#request.requestURI.matches(\'.*/auths/?\') and #request.getMethod().equals(\'POST\') ? \'UpdateAuth\' :' +
                                    '#request.requestURI.matches(\'.*/auths/?\') and #request.getMethod().equals(\'DELETE\') ? \'DeleteAuth\' :' +
                                    '#request.requestURI.matches(\'.*/status/?\') and #request.getMethod().equals(\'GET\') ? \'SystemStatus\' :' +
                                    'null']
        }
    }

    // ================= DAO =====================================================================================

    databaseExceptionHandler(DatabaseExceptionHandler) {
        databaseHost = '${ru.forxy.auth.MongoTemplate/host}:${ru.forxy.auth.MongoTemplate/port}'
    }

    daoLogger(LogWriter, 'auth.performance.dao')

    daoLoggingInterceptor(LoggingInterceptor) {
        configuration = ref(performanceConfiguration)
        responseWriter = ref(daoLogger)
        exceptionHandler = ref(databaseExceptionHandler)
    }

    // ================= IMPLEMENTATION ==========================================================================

    serviceExceptionHandler(ServiceExceptionHandler)

    serviceLogger(LogWriter, 'auth.performance.service.impl')

    serviceLoggingInterceptor(LoggingInterceptor) {
        configuration = ref(performanceConfiguration)
        responseWriter = ref(serviceLogger)
        exceptionHandler = ref(serviceExceptionHandler)
    }
}