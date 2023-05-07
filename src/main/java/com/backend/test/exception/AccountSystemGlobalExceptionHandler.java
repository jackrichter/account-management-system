package com.backend.test.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.NonUniqueResultException;
import java.time.LocalDateTime;

@ControllerAdvice
public class AccountSystemGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request) throws Exception {

        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleAccountNotFoundException(Exception ex, WebRequest request) throws Exception {

        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public final ResponseEntity<ErrorDetails> handleAccountAlreadyExistsException(Exception ex, WebRequest request) throws Exception {

        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BalanceExceededException.class)
    public final ResponseEntity<ErrorDetails> handleBalanceExceededException(Exception ex, WebRequest request) throws Exception {

        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // Handles Validation errors
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String message = "Total Errors: " + ex.getErrorCount() + " First Error: " + ex.getFieldError().getDefaultMessage();
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), message, request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
