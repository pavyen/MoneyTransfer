package com.home.task.moneytransfer.services.impl;

import com.home.task.moneytransfer.models.Account;
import com.home.task.moneytransfer.models.Transaction;
import com.home.task.moneytransfer.repository.TransactionDao;
import com.home.task.moneytransfer.services.AccountService;
import com.home.task.moneytransfer.services.TransferService;
import com.home.task.moneytransfer.utils.ValidationConstants;
import com.home.task.moneytransfer.validator.TransactionValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Service for money transfer.
 */
@AllArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {

    private AccountService accountService;
    private TransactionDao transactionDao;
    private ReadWriteLock reentrantLock;

    /**
     * Transfer money between accounts.
     *
     * @param transaction Transaction object.
     * @return result of transaction.
     */
    @Override
    public boolean transferMoney(final Transaction transaction) {

        TransactionValidator.transactionIsNotNull()
                .and(TransactionValidator.accountIdFromIsNotBlank())
                .and(TransactionValidator.accountIdToIsNotBlank())
                .and(TransactionValidator.accountsShouldBeDifferent())
                .and(TransactionValidator.amountIsNotNull())
                .and(TransactionValidator.amountIsPositive())
                .apply(transaction);
        //Start lock
        Lock lock = reentrantLock.writeLock();
        lock.lock();
        try {
            final Account accountFrom = Optional.ofNullable(accountService.getAccount(transaction.getAccountIdFrom()))
                    .orElseThrow(() -> new IllegalArgumentException(ValidationConstants.WRONG_ACCOUNT_FROM_ID));
            final Account accountTo = Optional.ofNullable(accountService.getAccount(transaction.getAccountIdTo()))
                    .orElseThrow(() -> new IllegalArgumentException(ValidationConstants.WRONG_ACCOUNT_TO_ID));
            final BigDecimal amount = transaction.getAmount();

            //withdraw
            accountFrom.withdraw(amount);
            //deposit
            accountTo.deposit(amount);
            //set transaction date
            transaction.setTransactionDate(LocalDateTime.now(ZoneOffset.UTC));

            accountService.saveAccount(accountFrom);
            accountService.saveAccount(accountTo);

            final Transaction savedTransaction = transactionDao.create(transaction);
            log.info("Transaction complete: {}", savedTransaction);
        } finally {
            lock.unlock();
        }
        //End lock
        return true;
    }
}
