package br.com.agibank.customers.infrastructure.adapters.inbound.controllers;

import br.com.agibank.customers.api.v1.CustomerApiV1;
import br.com.agibank.customers.api.v1.model.*;
import br.com.agibank.customers.application.usecases.customer.CreateCustomerUseCase;
import br.com.agibank.customers.application.usecases.customer.FindAllCustomersUseCase;
import br.com.agibank.customers.application.usecases.customer.FindCustomerByIdUseCase;
import br.com.agibank.customers.application.usecases.customer.UpdateCustomerByIdUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CustomerControllerApiV1 implements CustomerApiV1 {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final FindAllCustomersUseCase findAllCustomersUseCase;
    private final FindCustomerByIdUseCase findCustomerByIdUseCase;
    private final UpdateCustomerByIdUseCase updateCustomerByIdUseCase;

    @Override
    public ResponseEntity<CustomerResponseDTO> createCustomer(final CustomerRequestDTO customerRequestDTO) {
        return ResponseEntity.status(CREATED).body(createCustomerUseCase.execute(customerRequestDTO));
    }

    @Override
    public ResponseEntity<PagedCustomerResponseDTO> findAllCustomers(final Integer page,
                                                                     final Integer size,
                                                                     final String sort) {
        return ResponseEntity.ok(findAllCustomersUseCase.execute(page, size, sort));
    }

    @Override
    public ResponseEntity<CustomerResponseDTO> findCustomerById(final Long customerId) {
        return ResponseEntity.ok(findCustomerByIdUseCase.execute(customerId));
    }

    @Override
    public ResponseEntity<UpdateCustomerResponseDTO> updateCustomerById(final Long customerId,
                                                                        final UpdateCustomerRequestDTO updateCustomerRequestDTO) {
        return ResponseEntity.ok(updateCustomerByIdUseCase.execute(customerId, updateCustomerRequestDTO));
    }
}