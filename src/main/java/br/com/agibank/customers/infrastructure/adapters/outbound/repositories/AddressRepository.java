package br.com.agibank.customers.infrastructure.adapters.outbound.repositories;

import br.com.agibank.customers.infrastructure.adapters.outbound.entities.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
}
