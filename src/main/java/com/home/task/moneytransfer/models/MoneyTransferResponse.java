package com.home.task.moneytransfer.models;

import lombok.Data;

/**
 * Object to wrap operations results.
 */
@Data
public class MoneyTransferResponse {
    private RequestError error;
    private boolean success;
}
