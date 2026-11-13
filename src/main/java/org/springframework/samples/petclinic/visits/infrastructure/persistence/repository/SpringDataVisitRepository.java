package org.springframework.samples.petclinic.visits.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.visits.infrastructure.persistence.entity.VisitEntity;
import java.util.List;

public interface SpringDataVisitRepository extends JpaRepository<VisitEntity, Long> {
	List<VisitEntity> findByPetId(Long petId);
}
