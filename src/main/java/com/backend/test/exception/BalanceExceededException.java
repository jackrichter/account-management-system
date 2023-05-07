package com.backend.test.exception;

public class BalanceExceededException extends Exception {
    public BalanceExceededException(String message) {
        super(message);
    }
}
