package com.home.task.moneytransfer.controllers;

import com.home.task.moneytransfer.models.Account;
import com.home.task.moneytransfer.services.AccountService;
import com.home.task.moneytransfer.utils.Constants;
import lombok.AllArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;
import spark.Spark;

/**
 * Controller to handle accounts endpoints.
 */
@AllArgsConstructor
public class AccountsController extends AbstractController {

    private AccountService accountService;

    /**
     * Initialisation accounts routs.
     * Return account by id:    /moneytransfer/accounts/:id
     * Return list of accounts: /moneytransfer/accounts
     */
    public void initRouts(){
        Spark.get(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS + "/:id", (request, response) -> {
            response.status(HttpStatus.OK_200);
            response.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
            return mapper.writeValueAsString(accountService.getAccount(request.params("id")));
        });
        Spark.post(Constants.CONTEXT_MONEYTRANSFER_ACCOUNTS, Constants.APPLICATION_JSON, (request, response) -> {
            response.status(HttpStatus.CREATED_201);
            response.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
            return mapper.writeValueAsString(accountService.saveAccount(mapper.readValue(request.body(), Account.class)));
        });
    }
}
