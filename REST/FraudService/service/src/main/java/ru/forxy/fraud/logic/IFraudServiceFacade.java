package ru.forxy.fraud.logic;

import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.pojo.SortDirection;
import ru.forxy.fraud.rest.v1.check.Transaction;

/**
 * Entry point into fraud service business logic
 */
public interface IFraudServiceFacade {

    Boolean check(final Transaction transaction);

    Iterable<Transaction> getAllTransactions();

    EntityPage<Transaction> getTransactions(final Integer page, final Integer size, final SortDirection sortDirection,
                                            final String sortedBy, final Transaction filter);

    Transaction getTransaction(final String clientID);
}
