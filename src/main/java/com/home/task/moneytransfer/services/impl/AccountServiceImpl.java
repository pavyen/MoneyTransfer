package com.home.task.moneytransfer.services.impl;

import com.home.task.moneytransfer.models.Account;
import com.home.task.moneytransfer.repository.AccountDao;
import com.home.task.moneytransfer.services.AccountService;
import com.home.task.moneytransfer.utils.ValidationConstants;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.concurrent.locks.Lock;

@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;
    private Lock reentrantLock;

    /**
     * Returns account from DataSource.
     *
     * @param id account Id.
     * @return Account object.
     */
    @Override
    public Account getAccount(final String id) {
        reentrantLock.lock();
        try {
            return accountDao.get(id);
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * Returns accounts list from DataSource.
     *
     * @return Account object.
     */
    @Override
    public List<Account> getAccounts() {
        reentrantLock.lock();
        try {
            return accountDao.getAll();
        } finally {
            reentrantLock.unlock();
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
        reentrantLock.lock();
        try {
            if (account.getId() != null) {
                return accountDao.update(account);
            } else {
                return accountDao.create(account);
            }
        } finally {
            reentrantLock.unlock();
        }
    }

}
