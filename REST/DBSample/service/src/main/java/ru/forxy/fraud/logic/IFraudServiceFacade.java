package ru.forxy.fraud.logic;

import ru.forxy.common.pojo.EntityPage;
import ru.forxy.fraud.rest.pojo.Transaction;

/**
 * Entry point into fraud service business logic
 */
public interface IFraudServiceFacade {

    Boolean check(final Transaction transaction);

    EntityPage<Transaction> getTransactions(final Integer page);

    EntityPage<Transaction> getTransactions(final Integer page, final Integer size);

    Transaction getTransaction(final Long id);
}
