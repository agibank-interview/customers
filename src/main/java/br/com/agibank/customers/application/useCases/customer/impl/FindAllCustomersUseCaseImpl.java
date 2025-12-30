package br.com.agibank.customers.application.useCases.customer.impl;

import br.com.agibank.customers.api.v1.model.PagedCustomerResponseDTO;
import br.com.agibank.customers.application.useCases.customer.FindAllCustomersUseCase;
import br.com.agibank.customers.infrastructure.adapters.outbound.repositories.CustomerRepository;
import br.com.agibank.customers.infrastructure.mappers.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.valueOf;
import static org.springframework.data.domain.Sort.by;

@Service
@RequiredArgsConstructor
public class FindAllCustomersUseCaseImpl implements FindAllCustomersUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional(readOnly = true)
    public PagedCustomerResponseDTO execute(final Integer page,
                                            final Integer size,
                                            final String sort) {
        final String[] splitSort = sort.split(",");
        return customerMapper.toPagedCustomerResponseDTO(customerRepository.findAll(
                of(page, size, by(valueOf(splitSort[1].toUpperCase()), splitSort[0].toLowerCase()))));
    }
}
