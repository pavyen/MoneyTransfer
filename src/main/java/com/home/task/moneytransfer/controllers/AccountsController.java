package com.home.task.moneytransfer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.home.task.moneytransfer.models.Account;
import com.home.task.moneytransfer.models.MoneyTransferResponse;
import com.home.task.moneytransfer.models.RequestError;
import com.home.task.moneytransfer.services.AccountService;
import com.home.task.moneytransfer.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.List;

/**
 * Controller to handle accounts endpoints.
 */
@AllArgsConstructor
@Slf4j
public class AccountsController extends AbstractController {

    private AccountService accountService;

    /**
     * Initialization accounts routs.
     * Return account by id:    /moneytransfer/accounts/:id
     * Return list of accounts: /moneytransfer/accounts
     */
    public void initRouts() {
        log.debug("Initialization accounts routs started.");
        Spark.get(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS + "/:id", this::getAccount);
        Spark.get(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS, (request, response) -> getAccounts(response));
        Spark.post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS, Constants.APPLICATION_JSON, this::createAccount);
        log.debug("Initialization accounts routs finished.");
    }

    /**
     * Return Account by given Id.
     */
    private Object getAccount(final Request request, final Response response) throws JsonProcessingException {
        final String accountId = request.params("id");
        final Account account = accountService.getAccount(accountId);
        if (account != null) {
            response.status(HttpStatus.OK_200);
            response.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
            log.debug("Account with id={} was found.", accountId);
            return getMapper().writeValueAsString(account);
        }
        response.status(HttpStatus.NOT_FOUND_404);
        log.debug("Account with id={} wasn't found.", accountId);
        return StringUtils.EMPTY;
    }

    private Object getAccounts(final Response response) throws JsonProcessingException {
        final List<Account> accounts = accountService.getAccounts();
        if (accounts != null && !accounts.isEmpty()) {
            response.status(HttpStatus.OK_200);
            response.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
            log.debug("Found {} accounts.", accounts.size());
            return getMapper().writeValueAsString(accounts);
        }
        response.status(HttpStatus.NOT_FOUND_404);
        log.debug("No accounts was found.");
        return StringUtils.EMPTY;
    }

    private Object createAccount(final Request request, final Response response) throws java.io.IOException {
        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        try {
            final Account account = accountService.saveAccount(getMapper().readValue(request.body(), Account.class));
            response.status(HttpStatus.CREATED_201);
            moneyTransferResponse.setSuccess(true);
            log.debug("Account with id={} was created.", account.getId());
        } catch (IllegalArgumentException ex) {
            response.status(HttpStatus.BAD_REQUEST_400);
            response.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);

            moneyTransferResponse.setSuccess(false);
            moneyTransferResponse.setError(
                    new RequestError(ex.getMessage())
            );
            log.error(ex.getMessage(), ex);
        }
        return getMapper().writeValueAsString(moneyTransferResponse);
    }
}
