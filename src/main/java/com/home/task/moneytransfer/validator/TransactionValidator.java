package com.home.task.moneytransfer.validator;

import com.home.task.moneytransfer.models.Transaction;
import com.home.task.moneytransfer.utils.ValidationConstants;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

/**
 * Transfer data validator.
 */
public interface TransactionValidator extends Function<Transaction, Boolean> {

    static TransactionValidator transactionIsNotNull() {
        return transaction ->
                Optional.of(transaction != null)
                        .filter(Boolean::booleanValue)
                        .orElseThrow(() -> new IllegalArgumentException(ValidationConstants.TRANSACTION_SHOULD_NOT_BE_NULL));
    }

    static TransactionValidator accountIdFromIsNotBlank() {
        return transaction ->
                Optional.of(StringUtils.isNotBlank(transaction.getAccountIdFrom()))
                        .filter(Boolean::booleanValue)
                        .orElseThrow(() -> new IllegalArgumentException(ValidationConstants.ACCOUNT_ID_FROM_SHOULD_NOT_BE_BLANK));
    }

    static TransactionValidator accountIdToIsNotBlank() {
        return transaction ->
                Optional.of(StringUtils.isNotBlank(transaction.getAccountIdTo()))
                        .filter(Boolean::booleanValue)
                        .orElseThrow(() -> new IllegalArgumentException(ValidationConstants.ACCOUNT_ID_TO_SHOULD_NOT_BE_BLANK));
    }

    static TransactionValidator accountsShouldBeDifferent() {
        return transaction ->
                Optional.of(!StringUtils.equalsIgnoreCase(transaction.getAccountIdTo(), transaction.getAccountIdFrom()))
                        .filter(Boolean::booleanValue)
                        .orElseThrow(() -> new IllegalArgumentException(ValidationConstants.ACCOUNTS_SHOULD_BE_DIFFERENT));
    }

    static TransactionValidator amountIsNotNull() {
        return transaction ->
                Optional.of(transaction.getAmount() != null)
                        .filter(Boolean::booleanValue)
                        .orElseThrow(() -> new IllegalArgumentException(ValidationConstants.AMOUNT_SHOULD_NOT_BE_NULL));
    }

    static TransactionValidator amountIsPositive() {
        return transaction ->
                Optional.of(BigDecimal.ZERO.compareTo(transaction.getAmount()) < 0)
                        .filter(Boolean::booleanValue)
                        .orElseThrow(() -> new IllegalArgumentException(ValidationConstants.AMOUNT_SHOULD_BE_POSITIVE));
    }

    default TransactionValidator and(TransactionValidator other) {
        return user -> this.apply(user) && other.apply(user);
    }
}
