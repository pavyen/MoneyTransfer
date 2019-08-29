package com.home.task.moneytransfer.models;

import lombok.Data;

@Data
public class MoneyTransferResponse {
    private RequestError error;
    private boolean success;
}
