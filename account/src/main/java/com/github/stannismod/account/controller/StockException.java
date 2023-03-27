package com.github.stannismod.account.controller;

public class StockException extends RuntimeException {

    public StockException() {}

    public StockException(final String message) {
        super(message);
    }

    public StockException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public StockException(final Throwable cause) {
        super(cause);
    }

    public StockException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
