package org.springframework.samples.petclinic.owners.infrastructure.persistence.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.entity.OwnerJpaEntity;

import java.util.List;

public interface SpringDataOwnerRepository extends JpaRepository<OwnerJpaEntity, Long> {

	List<OwnerJpaEntity> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);

	long countByLastNameContainingIgnoreCase(String lastName);
}
