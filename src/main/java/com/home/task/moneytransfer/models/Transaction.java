package com.home.task.moneytransfer.models;

import lombok.*;

import java.math.BigDecimal;

/**
 * Class contains details of transfer operation.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @NonNull
    private BigDecimal amount;
    @NonNull
    private String accountIdFrom;
    @NonNull
    private String accountIdTo;
}
