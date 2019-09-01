package com.home.task.moneytransfer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.task.moneytransfer.controllers.AccountsController;
import com.home.task.moneytransfer.controllers.TransfersController;
import com.home.task.moneytransfer.models.Account;
import com.home.task.moneytransfer.repository.AccountDao;
import com.home.task.moneytransfer.repository.TransactionDao;
import com.home.task.moneytransfer.repository.impl.AccountDaoImpl;
import com.home.task.moneytransfer.repository.impl.TransactionDaoImpl;
import com.home.task.moneytransfer.services.AccountService;
import com.home.task.moneytransfer.services.TransferService;
import com.home.task.moneytransfer.services.impl.AccountServiceImpl;
import com.home.task.moneytransfer.services.impl.TransferServiceImpl;
import com.home.task.moneytransfer.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import spark.Spark;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class MoneyTransferApplication {

    private static final String ERROR_ON_LOADING_ACCOUNTS_LIST = "An error has occurred on loading Accounts list.";
    private static boolean initialised = false;

    private static AccountService accountService;

    private MoneyTransferApplication() {
    }

    public static void main(String[] args) {
        initApplication();
        initData();
    }

    /**
     * Initialize application.
     */
    public static void initApplication() {
        if (!initialised) {
            Spark.port(Constants.PORT);

            initControllers();
            log.info("Server is started.");
            initialised = true;
        }
    }

    public static AccountService getAccountService() {
        return accountService;
    }

    private static void initControllers() {
        final AccountDao accountDao = initAccountDataSource();
        final TransactionDao transactionDao = initTransactionDataSource();
        final Lock transactionLock = new ReentrantLock();

        accountService = new AccountServiceImpl(
                accountDao,
                transactionLock
        );
        TransferService transferService = new TransferServiceImpl(
                accountService,
                transactionDao,
                transactionLock
        );

        final AccountsController accountsController = new AccountsController(accountService);
        final TransfersController transfersController = new TransfersController(transferService);

        accountsController.initRouts();
        transfersController.initRouts();
    }

    /**
     * This method initialize custom Transaction DataSource.
     * @return TransactionDao implementation.
     */
    private static TransactionDao initTransactionDataSource() {
        return new TransactionDaoImpl();
    }

    /**
     * This method initialize custom Account DataSource.
     * @return AccountDao implementation.
     */
    private static AccountDao initAccountDataSource() {
        return new AccountDaoImpl();
    }

    /**
     * Initialize data.
     */
    public static void initData() {
        if (accountService != null) {
            loadData();
        }
    }

    /**
     * Load data form file.
     */
    private static void loadData() {
        final ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = AccountService.class.getResourceAsStream("/accounts.json")) {
            final Account[] accounts = mapper.readValue(inputStream, Account[].class);
            for (Account account : accounts) {
                accountService.saveAccount(account);
            }
        } catch (IOException ex) {
            log.error(ERROR_ON_LOADING_ACCOUNTS_LIST, ex);
        }
    }

}
