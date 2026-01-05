package br.com.agibank.customers.infrastructure.adapters.inbound.controllers;

import br.com.agibank.customers.api.v1.AddressApiV1;
import br.com.agibank.customers.api.v1.model.AddressResponseDTO;
import br.com.agibank.customers.api.v1.model.UpdateAddressRequestDTO;
import br.com.agibank.customers.application.usecases.address.UpdateAddressByIdUseCase;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.com.agibank.customers.infrastructure.adapters.inbound.controllers.CustomerControllerApiV1.CUSTOMERS_WRITE;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AddressControllerApiV1 implements AddressApiV1 {

    private final UpdateAddressByIdUseCase updateAddressByIdUseCase;

    @Override
    @RateLimiter(name = CUSTOMERS_WRITE)
    public ResponseEntity<AddressResponseDTO> updateAddressById(final Long addressId,
                                                                final UpdateAddressRequestDTO updateAddressRequestDTO) {
        return ResponseEntity.ok(updateAddressByIdUseCase.execute(addressId, updateAddressRequestDTO));
    }
}
