package ru.forxy.fraud.logic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.fraud.db.dao.IFraudDAO;
import ru.forxy.fraud.exceptions.FraudServiceExceptions;
import ru.forxy.fraud.rest.pojo.Transaction;

/**
 * Implementation class for FraudService business logic
 */
public class FraudServiceFacade implements IFraudServiceFacade {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private IFraudDAO fraudDAO;
	
	@Override
    public Boolean check(final Transaction transaction) {
	    return false;
	}

    @Override
    public Transaction getTransaction(final Long id) {
        if (id != null) {
            return fraudDAO.findOne(id);
        }
        throw new ServiceException(FraudServiceExceptions.IDSouldNotBeNull.getStatusTemplate());
    }

    @Override
    public EntityPage<Transaction> getTransactions(final Integer page) {
        return getTransactions(page, null);
    }

    @Override
    public EntityPage<Transaction> getTransactions(final Integer page, final Integer size) {
        final Page<Transaction> p = fraudDAO.findAll(new PageRequest(page, size == null ? DEFAULT_PAGE_SIZE : size));
        return new EntityPage<Transaction>(p.getContent(), p.getSize(), p.getNumber(), p.getTotalElements());
    }

    @Override
    public Transaction saveTransaction(final Transaction transaction) {
        return transaction;
    }

    public void setFraudDAO(final IFraudDAO fraudDAO) {
        this.fraudDAO = fraudDAO;
    }
}
