package com.home.task.moneytransfer.repository.impl;

import com.home.task.moneytransfer.models.Account;
import com.home.task.moneytransfer.repository.AccountDao;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation to manipulate with accounts. Java Map is used as Data Source.
 */
public class AccountDaoImpl implements AccountDao {

    private static final int INITIAL_CAPACITY = 20;
    private static final Map<String, Account> ACCOUNTS = new HashMap<>(INITIAL_CAPACITY);

    /**
     * Returns account from DataSource by id.
     *
     * @param id account Id.
     * @return Account object.
     */
    @Override
    public Account get(final String id) {
        final Account account = ACCOUNTS.get(id);
        if (account != null) {
            return account.toBuilder().build();
        }
        return null;
    }

    /**
     * Returns account list from DataSource.
     *
     * @return Accounts list.
     */
    @Override
    public List<Account> getAll() {
        return ACCOUNTS.values().stream()
                .map(account -> account.toBuilder().build())
                .collect(Collectors.toList());
    }

    /**
     * Create new Account entity in DataSource.
     *
     * @param sourceAccount Account object with data.
     * @return Account object created in DataSource.
     */
    @Override
    public Account create(final Account sourceAccount) {
        final Account account = sourceAccount.toBuilder().build();
        String id = Optional.ofNullable(account.getId()).orElse(UUID.randomUUID().toString());
        account.setId(id);
        ACCOUNTS.put(id, account);
        return account;
    }

    /**
     * Save Account object to DataSource.
     *
     * @param sourceAccount Account object with data.
     * @return Account object updated in DataSource.
     */
    @Override
    public Account update(final Account sourceAccount) {
        final Account account = sourceAccount.toBuilder().build();
        ACCOUNTS.put(account.getId(), account);
        return account;
    }
}
