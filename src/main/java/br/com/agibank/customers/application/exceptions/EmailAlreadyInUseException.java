package br.com.agibank.customers.application.exceptions;

import static java.lang.String.format;

public class EmailAlreadyInUseException extends BusinessConflictException {

    public static final String ERROR_MESSAGE = "Customer registration failed: Email '%s' is already in use";

    public EmailAlreadyInUseException(final String email) {
        super(format(ERROR_MESSAGE, email));
    }
}
