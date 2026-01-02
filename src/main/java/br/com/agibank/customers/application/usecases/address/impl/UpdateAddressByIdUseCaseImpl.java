package br.com.agibank.customers.application.usecases.address.impl;

import br.com.agibank.customers.api.v1.model.AddressResponseDTO;
import br.com.agibank.customers.api.v1.model.UpdateAddressRequestDTO;
import br.com.agibank.customers.application.exceptions.AddressNotFoundException;
import br.com.agibank.customers.application.usecases.address.UpdateAddressByIdUseCase;
import br.com.agibank.customers.infrastructure.adapters.outbound.entities.AddressEntity;
import br.com.agibank.customers.infrastructure.adapters.outbound.repositories.AddressRepository;
import br.com.agibank.customers.infrastructure.mappers.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateAddressByIdUseCaseImpl implements UpdateAddressByIdUseCase {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressResponseDTO execute(final Long addressId,
                                      final UpdateAddressRequestDTO updateAddressRequestDTO) {
        final AddressEntity addressEntity =
                addressRepository.findById(addressId).orElseThrow(() -> new AddressNotFoundException(addressId));
        addressMapper.fromUpdateAddressRequestDTO(updateAddressRequestDTO, addressEntity);
        return addressMapper.toAddressResponseDTO(addressRepository.save(addressEntity));
    }
}
