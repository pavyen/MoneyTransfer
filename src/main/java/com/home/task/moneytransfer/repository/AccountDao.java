package com.home.task.moneytransfer.repository;

import com.home.task.moneytransfer.models.Account;

/**
 * DAO to manipulate with Accounts in DataSource.
 */
public interface AccountDao {

    /**
     * Returns account from DataSource by id.
     *
     * @param id account Id.
     * @return Account object.
     */
    Account get(String id);

    /**
     * Create new Account entity in DataSource.
     * @param account Account object with data.
     * @return Account object created in DataSource.
     */
    Account create(Account account);

    /**
     * Save Account object to DataSource.
     * @param account Account object with data.
     * @return Account object created in DataSource.
     */
    Account update(Account account);
}
