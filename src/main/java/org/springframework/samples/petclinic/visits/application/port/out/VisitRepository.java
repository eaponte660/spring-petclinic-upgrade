package org.springframework.samples.petclinic.visits.application.port.out;

import org.springframework.samples.petclinic.visits.domain.model.Visit;

import java.util.List;

public interface VisitRepository {

	Visit save(Visit visit);

	List<Visit> findByPetId(Long petId);
}
