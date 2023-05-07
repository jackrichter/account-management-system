package com.backend.test.exception;

import javax.persistence.NonUniqueResultException;

public class AccountAlreadyExistsException extends Exception {
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}
