package br.com.agibank.customers.application.useCases.customer.impl;

import br.com.agibank.customers.api.v1.model.PagedCustomerResponseDTO;
import br.com.agibank.customers.infrastructure.adapters.outbound.entities.CustomerEntity;
import br.com.agibank.customers.infrastructure.adapters.outbound.repositories.CustomerRepository;
import br.com.agibank.customers.infrastructure.mappers.CustomerMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllCustomersUseCaseImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private FindAllCustomersUseCaseImpl findAllCustomersUseCase;

    @Test
    @DisplayName("Should find all customers successfully")
    void shouldFindAllCustomersSuccessfully() {
        final int page = 0;
        final int size = 10;
        final String sort = "id,asc";

        final CustomerEntity customerEntity = new CustomerEntity();
        final Page<CustomerEntity> customerPage = new PageImpl<>(List.of(customerEntity));
        final PagedCustomerResponseDTO pagedCustomerResponseDTO = new PagedCustomerResponseDTO();

        when(customerRepository.findAll(any(PageRequest.class))).thenReturn(customerPage);
        when(customerMapper.toPagedCustomerResponseDTO(customerPage)).thenReturn(pagedCustomerResponseDTO);

        final PagedCustomerResponseDTO result = findAllCustomersUseCase.execute(page, size, sort);

        assertNotNull(result);
        assertEquals(pagedCustomerResponseDTO, result);

        verify(customerRepository, times(1)).findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
        verify(customerMapper, times(1)).toPagedCustomerResponseDTO(customerPage);
    }

    @Test
    @DisplayName("Should find all customers with desc sort")
    void shouldFindAllCustomersWithDescSort() {
        final int page = 1;
        final int size = 20;
        final String sort = "name,desc";

        final Page<CustomerEntity> customerPage = new PageImpl<>(Collections.emptyList());
        final PagedCustomerResponseDTO responseDTO = new PagedCustomerResponseDTO();

        when(customerRepository.findAll(any(PageRequest.class))).thenReturn(customerPage);
        when(customerMapper.toPagedCustomerResponseDTO(customerPage)).thenReturn(responseDTO);

        final PagedCustomerResponseDTO result = findAllCustomersUseCase.execute(page, size, sort);

        assertNotNull(result);
        assertEquals(responseDTO, result);

        verify(customerRepository, times(1)).findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "name")));
    }

    @Test
    @DisplayName("should throw exception when sort format is invalid")
    void shouldThrowExceptionWhenSortFormatIsInvalid() {
        final int page = 0;
        final int size = 10;
        final String sort = "invalidFormat";
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> findAllCustomersUseCase.execute(page, size, sort));
    }
}
