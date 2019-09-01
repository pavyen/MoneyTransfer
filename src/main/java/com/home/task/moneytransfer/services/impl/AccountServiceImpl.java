package com.home.task.moneytransfer.services.impl;

import com.home.task.moneytransfer.models.Account;
import com.home.task.moneytransfer.repository.AccountDao;
import com.home.task.moneytransfer.services.AccountService;
import com.home.task.moneytransfer.utils.ValidationConstants;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Service to manipulate account data.
 */
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;
    private ReadWriteLock reentrantLock;

    /**
     * Returns account from DataSource.
     *
     * @param id account Id.
     * @return Account object.
     */
    @Override
    public Account getAccount(final String id) {
        //Lock is used because Account could be read for update during transaction.
        final Lock lock = reentrantLock.writeLock();
        lock.lock();
        try {
            return accountDao.get(id);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns accounts list from DataSource.
     *
     * @return Accounts list.
     */
    @Override
    public List<Account> getAccounts() {
        //Accounts list could be read during multiple transaction so data could not be actual.
        final Lock lock = reentrantLock.readLock();
        lock.lock();
        try {
            return accountDao.getAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Save or update account.
     *
     * @param account account value.
     */
    @Override
    public Account saveAccount(final Account account) {
        if (account.getBalance() == null) {
            throw new IllegalArgumentException(ValidationConstants.BALANCE_SHOULD_NOT_BE_NULL);
        }
        Lock lock = null;
        try {
            if (account.getId() != null) {
                lock = reentrantLock.writeLock();
                lock.lock();
                return accountDao.update(account);
            } else {
                lock = reentrantLock.readLock();
                lock.lock();
                return accountDao.create(account);
            }
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }

}
