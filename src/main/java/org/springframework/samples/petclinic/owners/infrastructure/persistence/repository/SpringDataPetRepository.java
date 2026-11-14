package org.springframework.samples.petclinic.owners.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.entity.PetJpaEntity;

public interface SpringDataPetRepository extends JpaRepository<PetJpaEntity, Long> {

	Page<PetJpaEntity> findByOwnerId(Long ownerId, Pageable pageable);

	boolean existsByOwnerIdAndNameIgnoreCase(Long ownerId, String name);

	@Query("SELECT COUNT(p) > 0 FROM PetJpaEntity p WHERE p.owner.id = :ownerId AND LOWER(p.name) = LOWER(:name) AND p.id <> :id")
	boolean existsByOwnerIdAndNameIgnoreCaseAndIdNot(
		@Param("ownerId") Long ownerId,
		@Param("name") String name,
		@Param("id") Long id
	);
}
