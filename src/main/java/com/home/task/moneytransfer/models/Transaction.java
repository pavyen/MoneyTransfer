package com.home.task.moneytransfer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Class contains details of transfer operation.
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private BigDecimal amount;
    private String accountIdFrom;
    private String accountIdTo;
    private String id;
    private LocalDateTime transactionDate;

}
