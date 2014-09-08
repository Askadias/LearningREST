package ru.forxy.fraud.db.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.forxy.common.status.ISystemStatusComponent;
import ru.forxy.fraud.rest.v1.Transaction;

/**
 * Data Access Object for Fraud database to manipulate Frauds.
 */
public interface ITransactionDAO extends PagingAndSortingRepository<Transaction, Long>, ISystemStatusComponent {
}

