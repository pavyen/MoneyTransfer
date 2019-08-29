package com.home.task.moneytransfer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Class contains details of transfer operation.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private BigDecimal amount;
    private String accountIdFrom;
    private String accountIdTo;
    private String id;
}
