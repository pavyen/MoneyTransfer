package com.home.task.moneytransfer.controllers;

import com.home.task.moneytransfer.models.Transaction;
import com.home.task.moneytransfer.services.TransferService;
import com.home.task.moneytransfer.utils.Constants;
import lombok.AllArgsConstructor;
import org.eclipse.jetty.http.HttpStatus;
import spark.Spark;

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
        Spark.post(Constants.CONTEXT_MONEYTRANSFER_TRANSFERS, Constants.APPLICATION_JSON, (request, response) -> {
            response.status(HttpStatus.NO_CONTENT_204);
            return mapper.writeValueAsString(transferService.transferMoney(mapper.readValue(request.body(), Transaction.class)));
        });
    }
}
