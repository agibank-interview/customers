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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateCustomerUseCaseImpl implements CreateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponseDTO execute(final CustomerRequestDTO customerRequestDTO) {
        log.info("Creating customer");

        if (customerRepository.existsByCpf(customerRequestDTO.getCpf())) {
            throw new CpfAlreadyInUseException(customerRequestDTO.getCpf());
        }
        if (customerRepository.existsByEmail(customerRequestDTO.getEmail())) {
            throw new EmailAlreadyInUseException(customerRequestDTO.getEmail());
        }
        final CustomerEntity customerEntity = customerMapper.toCustomerEntity(customerRequestDTO);
        final CustomerResponseDTO response = customerMapper.toCustomerResponseDTO(customerRepository.save(customerEntity));

        log.info("Customer created");
        return response;
    }
}
