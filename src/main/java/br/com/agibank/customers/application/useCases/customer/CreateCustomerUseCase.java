package br.com.agibank.customers.application.useCases.customer;

import br.com.agibank.customers.api.v1.model.CustomerRequestDTO;
import br.com.agibank.customers.api.v1.model.CustomerResponseDTO;

@FunctionalInterface
public interface CreateCustomerUseCase {

    CustomerResponseDTO execute(final CustomerRequestDTO customerRequestDTO);
}
