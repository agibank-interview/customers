package br.com.agibank.customers.application.exceptions;

import static java.lang.String.format;

public class AddressNotFoundException extends ResourceNotFoundException {

    public static final String ERROR_MESSAGE = "Address with id: '%d' not found";

    public AddressNotFoundException(final Long addressId) {
        super(format(ERROR_MESSAGE, addressId));
    }
}
