package br.com.agibank.customers.application.usecases.address;

import br.com.agibank.customers.api.v1.model.AddressResponseDTO;
import br.com.agibank.customers.api.v1.model.UpdateAddressRequestDTO;

@FunctionalInterface
public interface UpdateAddressByIdUseCase {
    AddressResponseDTO execute(final Long addressId,
                               final UpdateAddressRequestDTO updateAddressRequestDTO);

}
