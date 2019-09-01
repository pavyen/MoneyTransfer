package com.home.task.moneytransfer.utils;

/**
 * Contains constants for application.
 */
public final class Constants {

    private Constants() {
    }

    /**
     * Port number for Http Server.
     */
    public static final int PORT = 8182;

    /**
     * Accounts context path.
     */
    public static final String CONTEXT_MONEYTRANSFER_ACCOUNTS = "/moneytransfer/accounts";

    /**
     * Transfers context path.
     */
    public static final String CONTEXT_MONEYTRANSFER_TRANSFERS = "/moneytransfer/transfers";

    /**
     * 'application/json' header value.
     */
    public static final String APPLICATION_JSON = "application/json";

    /**
     * Content-Type header name.
     */
    public static final String CONTENT_TYPE = "Content-Type";
}
