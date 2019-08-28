package com.home.task.moneytransfer.models;

import lombok.*;

import java.math.BigDecimal;

/**
 * Class to store account data.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Account {
    @NonNull
    private String id;
    private String name;
    @NonNull
    private BigDecimal balance;
}
