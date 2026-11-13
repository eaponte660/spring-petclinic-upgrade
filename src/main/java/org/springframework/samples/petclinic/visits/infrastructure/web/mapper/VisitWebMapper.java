package org.springframework.samples.petclinic.visits.infrastructure.web.mapper;

import org.springframework.samples.petclinic.visits.domain.model.Visit;
import org.springframework.samples.petclinic.visits.infrastructure.web.dto.VisitResponse;
import org.springframework.stereotype.Component;

@Component
public class VisitWebMapper {

	public VisitResponse toResponse(Visit visit) {
		if (visit == null) {
			return null;
		}
		return new VisitResponse(
			visit.id(),
			visit.date(),
			visit.description(),
			visit.petId()
		);
	}
}
