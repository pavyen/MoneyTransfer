package com.home.task.moneytransfer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Class to store account data.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class Account {
    private String id;
    private BigDecimal balance;

    public void withdraw(final BigDecimal amount) {
        this.balance = balance.subtract(amount);
    }

    public void deposit(final BigDecimal amount) {
        this.balance = balance.add(amount);
    }
}
