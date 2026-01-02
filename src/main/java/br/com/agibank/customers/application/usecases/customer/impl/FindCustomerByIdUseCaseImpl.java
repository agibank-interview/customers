package br.com.agibank.customers.application.usecases.customer.impl;

import br.com.agibank.customers.api.v1.model.CustomerResponseDTO;
import br.com.agibank.customers.application.exceptions.CustomerNotFoundException;
import br.com.agibank.customers.application.usecases.customer.FindCustomerByIdUseCase;
import br.com.agibank.customers.infrastructure.adapters.outbound.repositories.CustomerRepository;
import br.com.agibank.customers.infrastructure.mappers.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindCustomerByIdUseCaseImpl implements FindCustomerByIdUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO execute(final Long customerId) {
        return customerMapper.toCustomerResponseDTO(customerRepository.findWithAddressesById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId)));
    }
}
