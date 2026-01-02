package br.com.agibank.customers.application.usecases.address.impl;

import br.com.agibank.customers.api.v1.model.AddressResponseDTO;
import br.com.agibank.customers.api.v1.model.UpdateAddressRequestDTO;
import br.com.agibank.customers.application.exceptions.AddressNotFoundException;
import br.com.agibank.customers.infrastructure.adapters.outbound.entities.AddressEntity;
import br.com.agibank.customers.infrastructure.adapters.outbound.repositories.AddressRepository;
import br.com.agibank.customers.infrastructure.mappers.AddressMapper;
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
class UpdateAddressByIdUseCaseImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private UpdateAddressByIdUseCaseImpl updateAddressByIdUseCase;

    @Test
    @DisplayName("Should update address successfully")
    void shouldUpdateAddressSuccessfully() {
        final Long addressId = 1L;
        final UpdateAddressRequestDTO updateAddressRequestDTO = new UpdateAddressRequestDTO();
        final AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(addressId);
        final AddressResponseDTO addressResponseDTO = new AddressResponseDTO();

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(addressEntity));
        when(addressRepository.save(addressEntity)).thenReturn(addressEntity);
        when(addressMapper.toAddressResponseDTO(addressEntity)).thenReturn(addressResponseDTO);

        final AddressResponseDTO result = updateAddressByIdUseCase.execute(addressId, updateAddressRequestDTO);

        assertNotNull(result);
        assertEquals(addressResponseDTO, result);

        verify(addressRepository, times(1)).findById(addressId);
        verify(addressMapper, times(1)).fromUpdateAddressRequestDTO(updateAddressRequestDTO, addressEntity);
        verify(addressRepository, times(1)).save(addressEntity);
        verify(addressMapper, times(1)).toAddressResponseDTO(addressEntity);
    }

    @Test
    @DisplayName("Should throw AddressNotFoundException when address not found")
    void shouldThrowExceptionWhenAddressNotFound() {
        final Long addressId = 1L;
        final UpdateAddressRequestDTO updateAddressRequestDTO = new UpdateAddressRequestDTO();

        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        final AddressNotFoundException exception =
                assertThrows(AddressNotFoundException.class, () ->
                        updateAddressByIdUseCase.execute(addressId, updateAddressRequestDTO));

        assertEquals(format(AddressNotFoundException.ERROR_MESSAGE, addressId), exception.getMessage());

        verify(addressRepository, times(1)).findById(addressId);
        verify(addressMapper, never()).fromUpdateAddressRequestDTO(any(), any());
        verify(addressRepository, never()).save(any());
    }
}
