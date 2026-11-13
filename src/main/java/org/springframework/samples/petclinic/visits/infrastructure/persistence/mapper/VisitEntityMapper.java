package org.springframework.samples.petclinic.visits.infrastructure.persistence.mapper;

import org.springframework.samples.petclinic.visits.domain.model.Visit;
import org.springframework.samples.petclinic.visits.infrastructure.persistence.entity.VisitEntity;
import org.springframework.stereotype.Component;

@Component
public class VisitEntityMapper {

	public Visit toDomain(VisitEntity entity) {
		if (entity == null) {
			return null;
		}
		return new Visit(
			entity.getId(),
			entity.getDate(),
			entity.getDescription(),
			entity.getPetId()
		);
	}

	public VisitEntity toEntity(Visit domain) {
		if (domain == null) {
			return null;
		}
		var entity = new VisitEntity();
		entity.setId(domain.id());
		entity.setDate(domain.date());
		entity.setDescription(domain.description());
		entity.setPetId(domain.petId());
		return entity;
	}
}
