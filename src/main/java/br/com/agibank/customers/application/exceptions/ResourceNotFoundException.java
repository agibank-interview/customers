package br.com.agibank.customers.application.exceptions;

import org.springframework.http.HttpStatus;

public abstract class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(final String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
