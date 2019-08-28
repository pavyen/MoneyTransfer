package com.home.task.moneytransfer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.task.moneytransfer.controllers.AccountsController;
import com.home.task.moneytransfer.controllers.TransfersController;
import com.home.task.moneytransfer.models.Account;
import com.home.task.moneytransfer.services.AccountService;
import com.home.task.moneytransfer.services.TransferService;
import com.home.task.moneytransfer.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import spark.Spark;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class MoneyTransferApplication {

    private static final String ERROR_ON_LOADING_ACCOUNTS_LIST = "MoneyTransferError on loading Accounts list.";

    private final static AccountService accountService = null;

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
        Spark.port(Constants.PORT);
        initControllers();
        log.info("Server is started.");
    }

    private static void initControllers() {
        final AccountsController accountsController = new AccountsController(accountService);
        accountsController.initRouts();

        final TransferService transferService = null;
        final TransfersController transfersController = new TransfersController(transferService);
        transfersController.initRouts();
    }


    /**
     * Initialize data.
     */
    private static void initData() {
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

    /**
     * Stop HttpServer.
     */
    public static void stopServer() {
        Spark.stop();
        log.info("Server is stopped.");
    }
}
