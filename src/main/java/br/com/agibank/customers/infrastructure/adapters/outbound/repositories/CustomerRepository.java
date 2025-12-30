package br.com.agibank.customers.infrastructure.adapters.outbound.repositories;

import br.com.agibank.customers.infrastructure.adapters.outbound.entities.CustomerEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    boolean existsByCpf(final String cpf);

    boolean existsByEmail(final String email);

    @EntityGraph(attributePaths = {"addresses"})
    Optional<CustomerEntity> findWithAddressesById(final Long customerId);
}
