package ru.forxy.common;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.UUID;

/**
 * Base class for rest service endpoint implementation
 */
public abstract class AbstractService {

    protected static final String TRANSACTION_GUID = RequestHelper.Param.TRANSACTION_GUID.getHttpHeaderName();
    protected static final String MESSAGE_GUID = RequestHelper.Param.MESSAGE_GUID.getHttpHeaderName();
    protected static final String SERVICE_VERSION = RequestHelper.Param.SERVICE_VERSION.getHttpHeaderName();
    protected static final String RESPONDER_ID = RequestHelper.Param.RESPONDER_ID.getHttpHeaderName();
    protected static final String CLIENT_ID = RequestHelper.Param.SERVICE_VERSION.getHttpHeaderName();

    protected String getTransactionGUID(final HttpHeaders headers, final UriInfo uriInfo)
    {
        return RequestHelper.getRequestValue(RequestHelper.Param.TRANSACTION_GUID, uriInfo, headers, UUID.randomUUID()
                .toString());
    }

    protected String getMessageGUID(final HttpHeaders headers, final UriInfo uriInfo)
    {
        return RequestHelper.getRequestValue(RequestHelper.Param.MESSAGE_GUID, uriInfo, headers);
    }

    /**
     * Creates the <tt>successful</tt> response builder with the response entity and pre-populated
     * response headers (see {@link #populateHeaders} method).
     * @param response Response entity
     * @param transactionGUID Transaction GUID
     * @param uriInfo URI Info provided by the JAX-RS infrastructure
     * @param headers HTTP Headers provided by the JAX-RS infrastructure
     * @return Response builder created
     */
    protected <RS> Response.ResponseBuilder respondWith(final RS response, final String transactionGUID,
                                               final UriInfo uriInfo, final HttpHeaders headers)
    {
        final Response.ResponseBuilder builder = Response.ok(response);
        return populateHeaders(builder, transactionGUID, uriInfo, headers);
    }

    /**
     * Creates the response builder with the provided response status and pre-populated
     * response headers (see {@link #populateHeaders} method).
     * @param status Response status
     * @param transactionGUID Transaction GUID
     * @param uriInfo URI Info provided by the JAX-RS infrastructure
     * @param headers HTTP Headers provided by the JAX-RS infrastructure
     * @return Response builder created
     */
    protected Response.ResponseBuilder respondWith(final Response.Status status, final String transactionGUID,
                                               final UriInfo uriInfo, final HttpHeaders headers)
    {
        final Response.ResponseBuilder builder = Response.status(status);
        return populateHeaders(builder, transactionGUID, uriInfo, headers);
    }

    /**
     * Populates the following response headers:
     * <ul>
     * <li>Transaction-GUID</li>
     * <li>Message-GUID</li>
     * <li>Service-Version</li>
     * <li>Responder-ID</li>
     * </ul>
     * @param builder Response builder, whose headers are to be populated
     * @param transactionGUID Transaction GUID
     * @param uriInfo URI Info provided by the JAX-RS infrastructure
     * @param headers HTTP Headers provided by the JAX-RS infrastructure
     * @return Response builder
     */
    protected Response.ResponseBuilder populateHeaders(Response.ResponseBuilder builder, final String transactionGUID,
                                              final UriInfo uriInfo, final HttpHeaders headers)
    {
        builder.header(TRANSACTION_GUID, transactionGUID);
        final String messageGUID = getMessageGUID(headers, uriInfo);
        if (messageGUID != null && !"".equals(messageGUID))
        {
            builder = builder.header(MESSAGE_GUID, messageGUID);
        }
        builder.header(SERVICE_VERSION, SystemProperties.getServiceVersion());
        builder.header(RESPONDER_ID, SystemProperties.getMachineID());
        return builder;
    }
}
