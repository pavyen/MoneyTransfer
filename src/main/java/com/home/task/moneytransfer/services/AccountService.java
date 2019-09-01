package com.home.task.moneytransfer.services;

import com.home.task.moneytransfer.models.Account;

import java.util.List;

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
     * Returns accounts list from DataSource.
     *
     * @return Account object.
     */
    List<Account> getAccounts();

    /**
     * Save or update account.
     *
     * @param account account value.
     * @return Account object.
     */
    Account saveAccount(Account account);
}
