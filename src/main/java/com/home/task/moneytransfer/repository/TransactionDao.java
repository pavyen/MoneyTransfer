package com.home.task.moneytransfer.repository;

import com.home.task.moneytransfer.models.Transaction;

public interface TransactionDao {

    /**
     * Returns transaction from DataSource by id.
     * @return Transaction object.
     */
    Transaction get(String id);

    /**
     * Create new Transaction entity in DataSource.
     * @param transaction Transaction object with data.
     * @return Transaction object created in DataSource.
     */
    Transaction create(Transaction transaction);
}
