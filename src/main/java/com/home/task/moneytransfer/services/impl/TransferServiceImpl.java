package com.home.task.moneytransfer.services.impl;

import com.home.task.moneytransfer.models.Transaction;
import com.home.task.moneytransfer.repository.AccountDao;
import com.home.task.moneytransfer.repository.TransactionDao;
import com.home.task.moneytransfer.services.TransferService;
import com.home.task.moneytransfer.validator.TransactionValidator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TransferServiceImpl implements TransferService {

    private AccountDao accountDao;
    private TransactionDao transactionDao;

    /**
     * Transfer money between accounts.
     *
     * @param transaction Transaction object.
     * @return result of transaction.
     */
    @Override
    public boolean transferMoney(Transaction transaction) {
        TransactionValidator.transactionIsNotNull()
                .and(TransactionValidator.accountIdFromIsNotBlank())
                .and(TransactionValidator.accountIdToIsNotBlank())
                .and(TransactionValidator.accountsShouldBeDifferent())
                .and(TransactionValidator.amountIsNotNull())
                .and(TransactionValidator.amountIsPositive())
                .apply(transaction);


        return false;
    }
}
