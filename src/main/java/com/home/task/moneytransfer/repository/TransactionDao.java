package com.home.task.moneytransfer.repository;

import com.home.task.moneytransfer.models.Transaction;

/**
 * DAO to manipulate with Transactions in DataSource.
 */
public interface TransactionDao {

    /**
     * Create new Transaction entity in DataSource.
     * @param transaction Transaction object with data.
     * @return Transaction object created in DataSource.
     */
    Transaction create(Transaction transaction);
}
