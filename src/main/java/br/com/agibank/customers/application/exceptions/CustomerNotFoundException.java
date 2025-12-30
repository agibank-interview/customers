package br.com.agibank.customers.application.exceptions;

import static java.lang.String.format;

public class CustomerNotFoundException extends ResourceNotFoundException {

    public static final String ERROR_MESSAGE = "Customer with id: '%d' not found";

    public CustomerNotFoundException(final Long customerId) {
        super(format(ERROR_MESSAGE, customerId));
    }
}
