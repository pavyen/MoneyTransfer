package com.home.task.moneytransfer.controllers;

import com.home.task.moneytransfer.models.MoneyTransferResponse;
import com.home.task.moneytransfer.models.RequestError;
import com.home.task.moneytransfer.models.Transaction;
import com.home.task.moneytransfer.services.TransferService;
import com.home.task.moneytransfer.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.IOException;

/**
 * Controller to handle transfers endpoints.
 */
@AllArgsConstructor
@Slf4j
public class TransfersController extends AbstractController {

    private TransferService transferService;

    /**
     * Initialisation transfers routs.
     * Make transfer: /moneytransfer/transfers
     */
    public void initRouts() {
        log.debug("Initialization transfers routs started.");
        Spark.post(Constants.CONTEXT_MONEYTRANSFER_TRANSFERS, Constants.APPLICATION_JSON, this::processMoneyTransferRequest);
        log.debug("Initialization transfers routs finished.");
    }

    private String processMoneyTransferRequest(final Request request, final Response response) throws IOException {
        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        try {
            final Transaction transaction = getMapper().readValue(request.body(), Transaction.class);
            transferService.transferMoney(transaction);
            response.status(HttpStatus.CREATED_201);
            moneyTransferResponse.setSuccess(true);
            log.debug("Transfer from account {} to account {} has completed.", transaction.getAccountIdFrom(), transaction.getAccountIdTo());
        } catch (IllegalArgumentException ex) {
            response.status(HttpStatus.BAD_REQUEST_400);

            moneyTransferResponse.setSuccess(false);
            moneyTransferResponse.setError(
                    new RequestError(ex.getMessage())
            );
            log.error(ex.getMessage(), ex);
        }
        response.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
        return getMapper().writeValueAsString(moneyTransferResponse);
    }
}
