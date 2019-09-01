package com.home.task.moneytransfer.utils;

/**
 * Contains list of constants used for data validation.
 */
public final class ValidationConstants {

    private ValidationConstants() {
    }

    // Transfer validation
    public static final String TRANSACTION_SHOULD_NOT_BE_NULL = "Transaction should not be null.";
    public static final String ACCOUNT_ID_FROM_SHOULD_NOT_BE_BLANK = "Account Id From should not be blank.";
    public static final String ACCOUNT_ID_TO_SHOULD_NOT_BE_BLANK = "Account Id To should not be blank.";
    public static final String ACCOUNTS_SHOULD_BE_DIFFERENT = "Accounts should be different.";
    public static final String AMOUNT_SHOULD_NOT_BE_NULL = "Amount should not be null.";
    public static final String AMOUNT_SHOULD_BE_POSITIVE = "Amount should be Positive.";

    // Account validation
    public static final String WRONG_ACCOUNT_FROM_ID = "Wrong account From Id.";
    public static final String WRONG_ACCOUNT_TO_ID = "Wrong account To Id.";
    public static final String AMOUNT_TO_TRANSFER_IS_GREATER_THAN_ACCOUNT_BALANCE = "Amount to transfer is greater than account balance.";

    // Balance validation
    public static final String BALANCE_SHOULD_NOT_BE_NULL = "Balance should not be null.";

}
