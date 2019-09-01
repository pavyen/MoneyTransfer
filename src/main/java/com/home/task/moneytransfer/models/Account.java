package com.home.task.moneytransfer.models;

import com.home.task.moneytransfer.utils.ValidationConstants;
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

    /**
     * Withdraw money from account.
     * @param amount to withdraw.
     */
    public void withdraw(final BigDecimal amount) {
        if (amount.compareTo(this.balance) > 0) {
            throw new IllegalArgumentException(ValidationConstants.AMOUNT_TO_TRANSFER_IS_GREATER_THAN_ACCOUNT_BALANCE);
        }
        this.balance = balance.subtract(amount);
    }

    /**
     * Deposit money to account.
     * @param amount to deposit.
     */
    public void deposit(final BigDecimal amount) {
        this.balance = balance.add(amount);
    }
}
