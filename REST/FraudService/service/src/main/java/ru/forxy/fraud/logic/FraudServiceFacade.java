package ru.forxy.fraud.logic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.pojo.SortDirection;
import ru.forxy.fraud.db.dao.ITransactionDAO;
import ru.forxy.fraud.exceptions.FraudServiceEventLogId;
import ru.forxy.fraud.rest.v1.check.Transaction;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation class for FraudService business logic
 */
public class FraudServiceFacade implements IFraudServiceFacade {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private ITransactionDAO transactionDAO;

    @Override
    public Boolean check(final Transaction transaction) {
        transactionDAO.save(transaction);
        return false;
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> allTransactions = new LinkedList<>();
        for (Transaction transaction : transactionDAO.findAll()) {
            allTransactions.add(transaction);
        }
        return allTransactions;
    }

    @Override
    public EntityPage<Transaction> getTransactions(final Integer page, final Integer size, final SortDirection sortDirection,
                                                   final String sortedBy, final Transaction filter) {
        if (page >= 1) {
            int pageSize = size == null ? DEFAULT_PAGE_SIZE : size;
            PageRequest pageRequest;
            if (sortDirection != null && sortedBy != null) {
                Sort.Direction dir = sortDirection == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
                pageRequest = new PageRequest(page - 1, pageSize, dir, sortedBy);
            } else {
                pageRequest = new PageRequest(page - 1, pageSize);
            }
            final Page<Transaction> p = transactionDAO.findAll(pageRequest, filter);
            return new EntityPage<>(p.getContent(), p.getSize(), p.getNumber(), p.getTotalElements());
        } else {
            throw new ServiceException(FraudServiceEventLogId.InvalidPageNumber, page);
        }
    }

    @Override
    public Transaction getTransaction(final String transactionID) {
        Transaction transaction = transactionDAO.findOne(transactionID);
        if (transaction == null) {
            throw new ServiceException(FraudServiceEventLogId.TransactionNotFound, transactionID);
        }
        return transaction;
    }

    public void setTransactionDAO(final ITransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }
}
