package com.home.task.moneytransfer.services;

import com.home.task.moneytransfer.models.Transaction;

/**
 * Service to transfer.
 */
public interface TransferService {

    /**
     * Transfer money between accounts.
     * @param transaction transaction object.
     * @return result of transaction.
     */
    boolean transferMoney(Transaction transaction);
}
