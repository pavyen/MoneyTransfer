package com.home.task.moneytransfer.controllers;

import com.home.task.moneytransfer.models.MoneyTransferResponse;
import com.home.task.moneytransfer.models.RequestError;
import com.home.task.moneytransfer.models.Transaction;
import com.home.task.moneytransfer.services.TransferService;
import com.home.task.moneytransfer.utils.Constants;
import lombok.AllArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.IOException;

/**
 * Controller to handle transfers endpoints.
 */
@AllArgsConstructor
public class TransfersController extends AbstractController {

    private TransferService transferService;

    /**
     * Initialisation transfers routs.
     * Make transfer: /moneytransfer/transfers
     */
    public void initRouts(){
        Spark.post(Constants.CONTEXT_MONEYTRANSFER_TRANSFERS, Constants.APPLICATION_JSON, this::processMoneyTransferRequest);
    }

    private String processMoneyTransferRequest(final Request request, final Response response) throws IOException {
        final MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        try {
            transferService.transferMoney(mapper.readValue(request.body(), Transaction.class));
            response.status(HttpStatus.NO_CONTENT_204);
            moneyTransferResponse.setSuccess(true);
        } catch (IllegalArgumentException ex) {
            response.status(HttpStatus.BAD_REQUEST_400);
            response.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);

            moneyTransferResponse.setSuccess(false);
            moneyTransferResponse.setError(
                    RequestError.builder().message(ex.getMessage()).build()
            );
        }
        return mapper.writeValueAsString(moneyTransferResponse);
    }
}
