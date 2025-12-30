package br.com.agibank.customers.application.useCases.customer;

import br.com.agibank.customers.api.v1.model.UpdateCustomerRequestDTO;
import br.com.agibank.customers.api.v1.model.UpdateCustomerResponseDTO;

@FunctionalInterface
public interface UpdateCustomerByIdUseCase {

    UpdateCustomerResponseDTO execute(final Long customerId,
                                      final UpdateCustomerRequestDTO updateCustomerRequestDTO);
}
