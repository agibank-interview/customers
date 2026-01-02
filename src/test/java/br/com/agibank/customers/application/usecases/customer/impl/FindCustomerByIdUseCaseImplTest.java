package br.com.agibank.customers.application.usecases.customer.impl;

import br.com.agibank.customers.api.v1.model.CustomerResponseDTO;
import br.com.agibank.customers.application.exceptions.CustomerNotFoundException;
import br.com.agibank.customers.infrastructure.adapters.outbound.entities.CustomerEntity;
import br.com.agibank.customers.infrastructure.adapters.outbound.repositories.CustomerRepository;
import br.com.agibank.customers.infrastructure.mappers.CustomerMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindCustomerByIdUseCaseImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private FindCustomerByIdUseCaseImpl findCustomerByIdUseCase;

    @Test
    @DisplayName("Should find customer by id successfully")
    void shouldFindCustomerByIdSuccessfully() {
        final Long customerId = 1L;
        final CustomerEntity customerEntity = new CustomerEntity();
        final CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();

        when(customerRepository.findWithAddressesById(customerId)).thenReturn(Optional.of(customerEntity));
        when(customerMapper.toCustomerResponseDTO(customerEntity)).thenReturn(customerResponseDTO);

        final CustomerResponseDTO result = findCustomerByIdUseCase.execute(customerId);

        assertNotNull(result);
        assertEquals(customerResponseDTO, result);

        verify(customerRepository, times(1)).findWithAddressesById(customerId);
        verify(customerMapper, times(1)).toCustomerResponseDTO(customerEntity);
    }

    @Test
    @DisplayName("Should throw CustomerNotFoundException when customer not found")
    void shouldThrowExceptionWhenCustomerNotFound() {
        final Long customerId = 1L;

        when(customerRepository.findWithAddressesById(customerId)).thenReturn(Optional.empty());

        final CustomerNotFoundException exception =
                assertThrows(CustomerNotFoundException.class, () -> findCustomerByIdUseCase.execute(customerId));

        assertEquals(format(CustomerNotFoundException.ERROR_MESSAGE, customerId), exception.getMessage());

        verify(customerRepository, times(1)).findWithAddressesById(customerId);
    }
}
