package com.home.task.moneytransfer.repository.impl;

import com.home.task.moneytransfer.models.Transaction;
import com.home.task.moneytransfer.repository.TransactionDao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation to manipulate with transactions. Java Map is used as Data Source.
 */
public class TransactionDaoImpl implements TransactionDao {

    private static final int INITIAL_CAPACITY = 20;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Transaction> TRANSACTIONS = new HashMap<>(INITIAL_CAPACITY);

    /**
     * Create new Transaction entity in DataSource.
     *
     * @param sourceTransaction Transaction object with data.
     * @return Transaction object created in DataSource.
     */
    @Override
    public Transaction create(final Transaction sourceTransaction) {
        final Transaction transaction = sourceTransaction.toBuilder().build();
        String id = Optional.ofNullable(transaction.getId()).orElse(UUID.randomUUID().toString());
        transaction.setId(id);
        TRANSACTIONS.put(id, transaction);
        return transaction;
    }
}
