package org.springframework.samples.petclinic.owners.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.entity.PetTypeJpaEntity;

public interface SpringDataPetTypeRepository extends JpaRepository<PetTypeJpaEntity, Long> {
}
