package com.home.task.moneytransfer.services;

import com.home.task.moneytransfer.models.Account;

/**
 * Service to manipulate account data.
 */
public interface AccountService {

    /**
     * Returns account from DataSource.
     *
     * @param id account Id.
     * @return Account object.
     */
    Account getAccount(String id);

    /**
     * Save or update account.
     *
     * @param account account value.
     */
    Account saveAccount(Account account);
}
