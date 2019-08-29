package com.home.task.moneytransfer.services.impl;

import com.home.task.moneytransfer.models.Account;
import com.home.task.moneytransfer.repository.AccountDao;
import com.home.task.moneytransfer.services.AccountService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

    /**
     * Returns account from DataSource.
     *
     * @param id account Id.
     * @return Account object.
     */
    @Override
    public Account getAccount(final String id) {
        return null;
    }

    /**
     * Save or update account.
     *
     * @param account account value.
     */
    @Override
    public Account saveAccount(final Account account) {
        return null;
    }
}
