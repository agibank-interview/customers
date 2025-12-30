package br.com.agibank.customers.infrastructure.mappers;

import br.com.agibank.customers.api.v1.model.AddressRequestDTO;
import br.com.agibank.customers.api.v1.model.AddressResponseDTO;
import br.com.agibank.customers.api.v1.model.UpdateAddressRequestDTO;
import br.com.agibank.customers.infrastructure.adapters.outbound.entities.AddressEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    AddressEntity toEntity(final AddressRequestDTO dto);

    List<AddressEntity> toEntities(final List<AddressRequestDTO> dtos);

    AddressResponseDTO toAddressResponseDTO(final AddressEntity entity);

    List<AddressResponseDTO> toAddressResponseDTOs(final List<AddressEntity> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromUpdateAddressRequestDTO(final UpdateAddressRequestDTO dto,
                                     @MappingTarget final AddressEntity entity);
}