package ru.forxy.fraud.db.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.forxy.common.status.ISystemStatusComponent;
import ru.forxy.fraud.rest.v1.check.Transaction;

/**
 * Data Access Object for Fraud database to manipulate Frauds.
 */
public interface ITransactionDAO extends PagingAndSortingRepository<Transaction, String>, ISystemStatusComponent {

    Page<Transaction> findAll(final Pageable pageable, final Transaction filter);
}

