package br.com.agibank.customers.application.usecases.customer.impl;

import br.com.agibank.customers.api.v1.model.CustomerRequestDTO;
import br.com.agibank.customers.api.v1.model.CustomerResponseDTO;
import br.com.agibank.customers.application.exceptions.CpfAlreadyInUseException;
import br.com.agibank.customers.application.exceptions.EmailAlreadyInUseException;
import br.com.agibank.customers.application.usecases.customer.CreateCustomerUseCase;
import br.com.agibank.customers.infrastructure.adapters.outbound.entities.CustomerEntity;
import br.com.agibank.customers.infrastructure.adapters.outbound.repositories.CustomerRepository;
import br.com.agibank.customers.infrastructure.mappers.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateCustomerUseCaseImpl implements CreateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponseDTO execute(final CustomerRequestDTO customerRequestDTO) {
        if (customerRepository.existsByCpf(customerRequestDTO.getCpf())) {
            throw new CpfAlreadyInUseException(customerRequestDTO.getCpf());
        }
        if (customerRepository.existsByEmail(customerRequestDTO.getEmail())) {
            throw new EmailAlreadyInUseException(customerRequestDTO.getEmail());
        }
        final CustomerEntity customerEntity = customerMapper.toCustomerEntity(customerRequestDTO);
        return customerMapper.toCustomerResponseDTO(customerRepository.save(customerEntity));
    }
}
