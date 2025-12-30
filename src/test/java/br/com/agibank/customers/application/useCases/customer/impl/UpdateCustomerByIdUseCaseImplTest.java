package br.com.agibank.customers.application.useCases.customer.impl;

import br.com.agibank.customers.api.v1.model.UpdateCustomerRequestDTO;
import br.com.agibank.customers.api.v1.model.UpdateCustomerResponseDTO;
import br.com.agibank.customers.application.exceptions.CustomerNotFoundException;
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

import java.util.Optional;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCustomerByIdUseCaseImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private UpdateCustomerByIdUseCaseImpl updateCustomerByIdUseCase;

    @Test
    @DisplayName("Should update customer successfully when email is not changed")
    void shouldUpdateCustomerSuccessfullyWhenEmailIsNotChanged() {
        final Long customerId = 1L;
        final String email = "test@example.com";
        final UpdateCustomerRequestDTO updateCustomerRequestDTO = new UpdateCustomerRequestDTO();
        updateCustomerRequestDTO.setEmail(email);

        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customerId);
        customerEntity.setEmail(email);

        final UpdateCustomerResponseDTO updateCustomerResponseDTO = new UpdateCustomerResponseDTO();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));
        when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
        when(customerMapper.toUpdateCustomerResponseDTO(customerEntity)).thenReturn(updateCustomerResponseDTO);

        final UpdateCustomerResponseDTO result = updateCustomerByIdUseCase.execute(customerId, updateCustomerRequestDTO);

        assertNotNull(result);
        assertEquals(updateCustomerResponseDTO, result);

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, never()).existsByEmail(any());
        verify(customerMapper, times(1)).fromUpdateCustomerRequestDTO(updateCustomerRequestDTO, customerEntity);
        verify(customerRepository, times(1)).save(customerEntity);
        verify(customerMapper, times(1)).toUpdateCustomerResponseDTO(customerEntity);
    }

    @Test
    @DisplayName("Should update customer successfully when email is changed and not in use")
    void shouldUpdateCustomerSuccessfullyWhenEmailIsChangedAndNotInUse() {
        final Long customerId = 1L;
        final String oldEmail = "old@example.com";
        final String newEmail = "new@example.com";
        final UpdateCustomerRequestDTO updateCustomerRequestDTO = new UpdateCustomerRequestDTO();
        updateCustomerRequestDTO.setEmail(newEmail);

        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customerId);
        customerEntity.setEmail(oldEmail);

        final UpdateCustomerResponseDTO updateCustomerResponseDTO = new UpdateCustomerResponseDTO();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));
        when(customerRepository.existsByEmail(newEmail)).thenReturn(false);
        when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
        when(customerMapper.toUpdateCustomerResponseDTO(customerEntity)).thenReturn(updateCustomerResponseDTO);

        final UpdateCustomerResponseDTO result = updateCustomerByIdUseCase.execute(customerId, updateCustomerRequestDTO);

        assertNotNull(result);
        assertEquals(updateCustomerResponseDTO, result);

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).existsByEmail(newEmail);
        verify(customerMapper, times(1)).fromUpdateCustomerRequestDTO(updateCustomerRequestDTO, customerEntity);
        verify(customerRepository, times(1)).save(customerEntity);
        verify(customerMapper, times(1)).toUpdateCustomerResponseDTO(customerEntity);
    }

    @Test
    @DisplayName("Should throw CustomerNotFoundException when customer not found")
    void shouldThrowExceptionWhenCustomerNotFound() {
        final Long customerId = 1L;
        final UpdateCustomerRequestDTO updateCustomerRequestDTO = new UpdateCustomerRequestDTO();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        final CustomerNotFoundException exception =
                assertThrows(CustomerNotFoundException.class, () ->
                        updateCustomerByIdUseCase.execute(customerId, updateCustomerRequestDTO));

        assertEquals(format(CustomerNotFoundException.ERROR_MESSAGE, customerId), exception.getMessage());

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, never()).existsByEmail(any());
        verify(customerMapper, never()).fromUpdateCustomerRequestDTO(any(), any());
        verify(customerRepository, never()).save(any());
        verify(customerMapper, never()).toUpdateCustomerResponseDTO(any());
    }

    @Test
    @DisplayName("Should throw EmailAlreadyInUseException when new email is already in use")
    void shouldThrowExceptionWhenNewEmailIsAlreadyInUse() {
        final Long customerId = 1L;
        final String oldEmail = "old@example.com";
        final String newEmail = "new@example.com";
        final UpdateCustomerRequestDTO updateCustomerRequestDTO = new UpdateCustomerRequestDTO();
        updateCustomerRequestDTO.setEmail(newEmail);

        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customerId);
        customerEntity.setEmail(oldEmail);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));
        when(customerRepository.existsByEmail(newEmail)).thenReturn(true);

        final EmailAlreadyInUseException exception =
                assertThrows(EmailAlreadyInUseException.class, () ->
                        updateCustomerByIdUseCase.execute(customerId, updateCustomerRequestDTO));

        assertEquals(format(EmailAlreadyInUseException.ERROR_MESSAGE, updateCustomerRequestDTO.getEmail()), exception.getMessage());

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).existsByEmail(newEmail);
        verify(customerMapper, never()).fromUpdateCustomerRequestDTO(any(), any());
        verify(customerRepository, never()).save(any());
        verify(customerMapper, never()).toUpdateCustomerResponseDTO(any());
    }

    @Test
    @DisplayName("Should update customer successfully when email is null in request")
    void shouldUpdateCustomerSuccessfullyWhenEmailIsNull() {
        final Long customerId = 1L;
        final UpdateCustomerRequestDTO updateCustomerRequestDTO = new UpdateCustomerRequestDTO();
        updateCustomerRequestDTO.setEmail(null);

        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customerId);
        customerEntity.setEmail("test@example.com");

        final UpdateCustomerResponseDTO updateCustomerResponseDTO = new UpdateCustomerResponseDTO();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));
        when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
        when(customerMapper.toUpdateCustomerResponseDTO(customerEntity)).thenReturn(updateCustomerResponseDTO);

        final UpdateCustomerResponseDTO result = updateCustomerByIdUseCase.execute(customerId, updateCustomerRequestDTO);

        assertNotNull(result);
        assertEquals(updateCustomerResponseDTO, result);

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, never()).existsByEmail(any());
        verify(customerMapper, times(1)).fromUpdateCustomerRequestDTO(updateCustomerRequestDTO, customerEntity);
        verify(customerRepository, times(1)).save(customerEntity);
        verify(customerMapper, times(1)).toUpdateCustomerResponseDTO(customerEntity);
    }
}
