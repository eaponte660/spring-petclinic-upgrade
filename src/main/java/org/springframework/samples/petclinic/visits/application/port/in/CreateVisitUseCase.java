package org.springframework.samples.petclinic.visits.application.port.in;

import org.springframework.samples.petclinic.visits.domain.model.Visit;

import java.time.LocalDate;

public interface CreateVisitUseCase {

	Visit createVisit(CreateVisitCommand command);

	record CreateVisitCommand(
		LocalDate date,
		String description,
		Long petId
	) {}
}
