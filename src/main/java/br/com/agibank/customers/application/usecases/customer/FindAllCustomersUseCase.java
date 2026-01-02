package br.com.agibank.customers.application.usecases.customer;

import br.com.agibank.customers.api.v1.model.PagedCustomerResponseDTO;

@FunctionalInterface
public interface FindAllCustomersUseCase {

    PagedCustomerResponseDTO execute(final Integer page,
                                     final Integer size,
                                     final String sort);
}
