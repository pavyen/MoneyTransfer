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
     * Initialisation accounts routs.
     * Return account by id:    /moneytransfer/accounts/:id
     * Return list of accounts: /moneytransfer/accounts
     */
    public void initRouts(){
        Spark.get(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS + "/:id", this::getAccount);
        Spark.get(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS, this::getAccounts);
        Spark.post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS, Constants.APPLICATION_JSON, this::createAccount);
    }

    private Object getAccount(final Request request, final Response response) throws JsonProcessingException {
        final Account account = accountService.getAccount(request.params("id"));
        if (account != null) {
            response.status(HttpStatus.OK_200);
            response.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
            return mapper.writeValueAsString(account);
        }
        response.status(HttpStatus.NOT_FOUND_404);
        return StringUtils.EMPTY;
    }

    private Object getAccounts(final Request request, final Response response) throws JsonProcessingException {
        final List<Account> accounts = accountService.getAccounts();
        if (accounts != null && !accounts.isEmpty()) {
            response.status(HttpStatus.OK_200);
            response.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
            return mapper.writeValueAsString(accounts);
        }
        response.status(HttpStatus.NOT_FOUND_404);
        return StringUtils.EMPTY;
    }

    private Object createAccount(final Request request, final Response response) throws java.io.IOException {
        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        try {
            accountService.saveAccount(mapper.readValue(request.body(), Account.class));
            response.status(HttpStatus.CREATED_201);
            moneyTransferResponse.setSuccess(true);
        } catch (IllegalArgumentException ex) {
            response.status(HttpStatus.BAD_REQUEST_400);
            response.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);

            moneyTransferResponse.setSuccess(false);
            moneyTransferResponse.setError(
                    RequestError.builder().message(ex.getMessage()).build()
            );
            log.error(ex.getMessage(), ex);
        }
        return mapper.writeValueAsString(moneyTransferResponse);
    }
}
