package br.com.agibank.customers.application.exceptions;

import org.springframework.http.HttpStatus;

public abstract class BusinessConflictException extends BusinessException {

    public BusinessConflictException(final String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}
