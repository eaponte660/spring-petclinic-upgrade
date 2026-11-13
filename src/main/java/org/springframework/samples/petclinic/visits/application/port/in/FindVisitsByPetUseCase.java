package org.springframework.samples.petclinic.visits.application.port.in;

import org.springframework.samples.petclinic.visits.domain.model.Visit;

import java.util.List;

public interface FindVisitsByPetUseCase {

	List<Visit> findVisitsByPetId(FindVisitsByPetQuery query);

	record FindVisitsByPetQuery(Long petId) {}
}
