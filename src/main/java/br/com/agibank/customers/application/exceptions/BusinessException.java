package br.com.agibank.customers.application.exceptions;

import org.springframework.http.HttpStatus;

public abstract class BusinessException extends RuntimeException {

    public BusinessException(final String message) {
        super(message);
    }

    public abstract HttpStatus getHttpStatus();
}
