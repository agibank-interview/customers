package br.com.agibank.customers.application.useCases.customer;

import br.com.agibank.customers.api.v1.model.CustomerResponseDTO;

@FunctionalInterface
public interface FindCustomerByIdUseCase {

    CustomerResponseDTO execute(final Long customerId);
}
