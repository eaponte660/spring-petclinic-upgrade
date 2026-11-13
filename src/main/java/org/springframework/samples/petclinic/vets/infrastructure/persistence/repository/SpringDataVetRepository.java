package org.springframework.samples.petclinic.vets.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.vets.infrastructure.persistence.entity.VetEntity;

public interface SpringDataVetRepository extends JpaRepository<VetEntity, Long> {
}
