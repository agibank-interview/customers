package br.com.agibank.customers.application.useCases.customer.impl;

import br.com.agibank.customers.api.v1.model.CustomerRequestDTO;
import br.com.agibank.customers.api.v1.model.CustomerResponseDTO;
import br.com.agibank.customers.application.exceptions.CpfAlreadyInUseException;
import br.com.agibank.customers.application.exceptions.EmailAlreadyInUseException;
import br.com.agibank.customers.infrastructure.adapters.outbound.entities.CustomerEntity;
import br.com.agibank.customers.infrastructure.adapters.outbound.repositories.CustomerRepository;
import br.com.agibank.customers.infrastructure.mappers.CustomerMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCustomerUseCaseImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CreateCustomerUseCaseImpl createCustomerUseCase;

    @Test
    @DisplayName("Should throw CpfAlreadyInUseException when CPF already in use")
    void shouldThrowExceptionWhenCpfAlreadyInUse() {
        final CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setCpf("12345678901");

        when(customerRepository.existsByCpf(customerRequestDTO.getCpf())).thenReturn(true);

        final CpfAlreadyInUseException exception =
                assertThrows(CpfAlreadyInUseException.class, () -> createCustomerUseCase.execute(customerRequestDTO));

        assertEquals(format(CpfAlreadyInUseException.ERROR_MESSAGE, customerRequestDTO.getCpf()), exception.getMessage());

        verify(customerRepository, times(1)).existsByCpf(customerRequestDTO.getCpf());
        verify(customerRepository, never()).existsByEmail(any());
        verify(customerMapper, never()).toCustomerEntity(any());
        verify(customerRepository, never()).save(any());
        verify(customerMapper, never()).toCustomerResponseDTO(any());
    }

    @Test
    @DisplayName("Should throw EmailAlreadyInUseException when Email already in use")
    void shouldThrowExceptionWhenEmailAlreadyInUse() {
        final CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setCpf("12345678901");
        customerRequestDTO.setEmail("test@example.com");

        when(customerRepository.existsByCpf(customerRequestDTO.getCpf())).thenReturn(false);
        when(customerRepository.existsByEmail(customerRequestDTO.getEmail())).thenReturn(true);

        final EmailAlreadyInUseException exception =
                assertThrows(EmailAlreadyInUseException.class, () -> createCustomerUseCase.execute(customerRequestDTO));

        assertEquals(format(EmailAlreadyInUseException.ERROR_MESSAGE, customerRequestDTO.getEmail()), exception.getMessage());

        verify(customerRepository, times(1)).existsByCpf(customerRequestDTO.getCpf());
        verify(customerRepository, times(1)).existsByEmail(customerRequestDTO.getEmail());
        verify(customerMapper, never()).toCustomerEntity(any());
        verify(customerRepository, never()).save(any());
        verify(customerMapper, never()).toCustomerResponseDTO(any());
    }

    @Test
    @DisplayName("Should create customer successfully")
    void shouldCreateCustomerSuccessfully() {
        final CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setCpf("12345678901");
        customerRequestDTO.setEmail("test@example.com");

        final CustomerEntity customerEntity = new CustomerEntity();
        final CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();

        when(customerRepository.existsByCpf(customerRequestDTO.getCpf())).thenReturn(false);
        when(customerRepository.existsByEmail(customerRequestDTO.getEmail())).thenReturn(false);
        when(customerMapper.toCustomerEntity(customerRequestDTO)).thenReturn(customerEntity);
        when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
        when(customerMapper.toCustomerResponseDTO(customerEntity)).thenReturn(customerResponseDTO);

        final CustomerResponseDTO result = createCustomerUseCase.execute(customerRequestDTO);

        assertNotNull(result);
        assertEquals(customerResponseDTO, result);

        verify(customerRepository, times(1)).existsByCpf(customerRequestDTO.getCpf());
        verify(customerRepository, times(1)).existsByEmail(customerRequestDTO.getEmail());
        verify(customerMapper, times(1)).toCustomerEntity(customerRequestDTO);
        verify(customerRepository, times(1)).save(customerEntity);
        verify(customerMapper, times(1)).toCustomerResponseDTO(customerEntity);
    }
}
