package ru.forxy.fraud.rest;

import net.sf.oval.exception.ValidationFailedException;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.exceptions.ValidationException;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.rest.AbstractService;
import ru.forxy.common.support.Constants;
import ru.forxy.fraud.exceptions.FraudServiceExceptions;
import ru.forxy.fraud.logic.IFraudServiceFacade;
import ru.forxy.fraud.rest.pojo.Transaction;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;
import java.util.regex.Pattern;

public class FraudServiceImpl extends AbstractService implements IFraudService {

    private IFraudServiceFacade fraudServiceFacade;

    @Override
    public Response check(final Transaction transaction, final UriInfo uriInfo, final HttpHeaders headers) {
        return Response.ok(fraudServiceFacade.check(transaction)).build();
    }

    @Override
    public Response getTransactions(final Integer page, final UriInfo uriInfo, final HttpHeaders headers) {
        final EntityPage<Transaction> transactionsPage = fraudServiceFacade.getTransactions(page);
        return respondWith(transactionsPage, uriInfo, headers).build();
    }

    @Override
    public Response getTransactions(final Integer page, final Integer size, final UriInfo uriInfo, final HttpHeaders headers) {
        final EntityPage<Transaction> transactionsPage = fraudServiceFacade.getTransactions(page, size);
        return respondWith(transactionsPage, uriInfo, headers).build();
    }

    @Override
    public Response getTransaction(Long id, final UriInfo uriInfo, final HttpHeaders headers) {
        Transaction transaction = fraudServiceFacade.getTransaction(id);
        if (transaction == null) {
            throw new ServiceException(FraudServiceExceptions.TransactionNotFound.getStatusTemplate(), id);
        }
        return respondWith(transaction, uriInfo, headers).build();
    }

    public void setFraudServiceFacade(final IFraudServiceFacade fraudServiceFacade) {
        this.fraudServiceFacade = fraudServiceFacade;
    }
}
