package br.com.agibank.customers.application.exceptions;

import static java.lang.String.format;

public class CpfAlreadyInUseException extends BusinessConflictException {

    public static final String ERROR_MESSAGE = "Customer registration failed: CPF '%s' is already in use";

    public CpfAlreadyInUseException(final String cpf) {
        super(format(ERROR_MESSAGE, cpf));
    }
}
