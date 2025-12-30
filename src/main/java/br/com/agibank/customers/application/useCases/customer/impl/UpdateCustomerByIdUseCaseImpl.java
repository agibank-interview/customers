package br.com.agibank.customers.application.useCases.customer.impl;

import br.com.agibank.customers.api.v1.model.UpdateCustomerRequestDTO;
import br.com.agibank.customers.api.v1.model.UpdateCustomerResponseDTO;
import br.com.agibank.customers.application.exceptions.CustomerNotFoundException;
import br.com.agibank.customers.application.exceptions.EmailAlreadyInUseException;
import br.com.agibank.customers.application.useCases.customer.UpdateCustomerByIdUseCase;
import br.com.agibank.customers.infrastructure.adapters.outbound.entities.CustomerEntity;
import br.com.agibank.customers.infrastructure.adapters.outbound.repositories.CustomerRepository;
import br.com.agibank.customers.infrastructure.mappers.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateCustomerByIdUseCaseImpl implements UpdateCustomerByIdUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public UpdateCustomerResponseDTO execute(final Long customerId,
                                             final UpdateCustomerRequestDTO updateCustomerRequestDTO) {
        final CustomerEntity customerEntity = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        Optional.ofNullable(updateCustomerRequestDTO.getEmail())
                .filter(email -> !email.equalsIgnoreCase(customerEntity.getEmail()))
                .ifPresent(email -> {
                    if (customerRepository.existsByEmail(email)) {
                        throw new EmailAlreadyInUseException(email);
                    }
                });
        customerMapper.fromUpdateCustomerRequestDTO(updateCustomerRequestDTO, customerEntity);
        return customerMapper.toUpdateCustomerResponseDTO(customerRepository.save(customerEntity));
    }
}
