package br.com.agibank.customers.infrastructure.adapters.inbound.controllers;

import br.com.agibank.customers.api.v1.CustomerApiV1;
import br.com.agibank.customers.api.v1.model.*;
import br.com.agibank.customers.application.usecases.customer.CreateCustomerUseCase;
import br.com.agibank.customers.application.usecases.customer.FindAllCustomersUseCase;
import br.com.agibank.customers.application.usecases.customer.FindCustomerByIdUseCase;
import br.com.agibank.customers.application.usecases.customer.UpdateCustomerByIdUseCase;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CustomerControllerApiV1 implements CustomerApiV1 {

    public static final String CUSTOMERS_READ = "customers-read";
    public static final String CUSTOMERS_WRITE = "customers-write";

    private final CreateCustomerUseCase createCustomerUseCase;
    private final FindAllCustomersUseCase findAllCustomersUseCase;
    private final FindCustomerByIdUseCase findCustomerByIdUseCase;
    private final UpdateCustomerByIdUseCase updateCustomerByIdUseCase;

    @Override
    @RateLimiter(name = CUSTOMERS_WRITE)
    @Bulkhead(name = CUSTOMERS_WRITE)
    public ResponseEntity<CustomerResponseDTO> createCustomer(final CustomerRequestDTO customerRequestDTO) {
        return ResponseEntity.status(CREATED).body(createCustomerUseCase.execute(customerRequestDTO));
    }

    @Override
    @RateLimiter(name = CUSTOMERS_READ)
    @Bulkhead(name = CUSTOMERS_READ)
    public ResponseEntity<PagedCustomerResponseDTO> findAllCustomers(final Integer page,
                                                                     final Integer size,
                                                                     final String sort) {
        return ResponseEntity.ok(findAllCustomersUseCase.execute(page, size, sort));
    }

    @Override
    @RateLimiter(name = CUSTOMERS_READ)
    @Bulkhead(name = CUSTOMERS_READ)
    public ResponseEntity<CustomerResponseDTO> findCustomerById(final Long customerId) {
        return ResponseEntity.ok(findCustomerByIdUseCase.execute(customerId));
    }

    @Override
    @RateLimiter(name = CUSTOMERS_WRITE)
    @Bulkhead(name = CUSTOMERS_WRITE)
    public ResponseEntity<UpdateCustomerResponseDTO> updateCustomerById(final Long customerId,
                                                                        final UpdateCustomerRequestDTO updateCustomerRequestDTO) {
        return ResponseEntity.ok(updateCustomerByIdUseCase.execute(customerId, updateCustomerRequestDTO));
    }
}