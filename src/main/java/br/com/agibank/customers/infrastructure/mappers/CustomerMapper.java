package br.com.agibank.customers.infrastructure.mappers;

import br.com.agibank.customers.api.v1.model.*;
import br.com.agibank.customers.infrastructure.adapters.outbound.entities.AddressEntity;
import br.com.agibank.customers.infrastructure.adapters.outbound.entities.CustomerEntity;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Mapper(componentModel = "spring", uses = AddressMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    @BeanMapping(qualifiedByName = "setCustomerToAddress")
    CustomerEntity toCustomerEntity(final CustomerRequestDTO dto);

    CustomerResponseDTO toCustomerResponseDTO(final CustomerEntity entity);

    @Mapping(target = "page", expression = "java(mapPage(page))")
    @Mapping(source = "number", target = "pageNumber")
    @Mapping(target = "pageItems", expression = "java(mapPageItems(page))")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "totalElements", target = "totalItems")
    PagedCustomerResponseDTO toPagedCustomerResponseDTO(final Page<CustomerEntity> page);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromUpdateCustomerRequestDTO(final UpdateCustomerRequestDTO dto,
                                      @MappingTarget final CustomerEntity entity);

    UpdateCustomerResponseDTO toUpdateCustomerResponseDTO(final CustomerEntity entity);

    @AfterMapping
    @Named("setCustomerToAddress")
    default void setCustomerToAddress(@MappingTarget final CustomerEntity customer) {
        final List<AddressEntity> addresses = customer.getAddresses();
        if (!isEmpty(addresses)) {
            addresses.forEach(address -> address.setCustomer(customer));
        }
    }

    default List<CustomerResponseDTO> mapPage(final Page<CustomerEntity> page) {
        return page.getContent().stream()
                .map(this::toCustomerResponseDTO)
                .toList();
    }

    default int mapPageItems(final Page<CustomerEntity> page) {
        return page.getContent().size();
    }
}
