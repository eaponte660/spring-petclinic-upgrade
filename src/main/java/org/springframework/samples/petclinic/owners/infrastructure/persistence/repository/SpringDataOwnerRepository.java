package org.springframework.samples.petclinic.owners.infrastructure.persistence.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.entity.OwnerJpaEntity;

import java.util.List;
import java.util.Optional;

public interface SpringDataOwnerRepository extends JpaRepository<OwnerJpaEntity, Long> {

	List<OwnerJpaEntity> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);

	long countByLastNameContainingIgnoreCase(String lastName);

	@Query("SELECT o FROM OwnerJpaEntity o LEFT JOIN FETCH o.pets p LEFT JOIN FETCH p.type WHERE o.id = :id")
	Optional<OwnerJpaEntity> findByIdWithPets(@Param("id") Long id);
}
