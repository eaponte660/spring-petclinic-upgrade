package org.springframework.samples.petclinic.owners.infrastructure.persistence.mapper;

import org.springframework.samples.petclinic.owners.domain.model.PetType;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.entity.PetTypeJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PetTypeEntityMapper {

	public PetType toDomain(PetTypeJpaEntity entity) {
		if (entity == null) {
			return null;
		}
		return new PetType(entity.getId(), entity.getName());
	}

	public PetTypeJpaEntity toEntity(PetType domain) {
		if (domain == null) {
			return null;
		}
		var entity = new PetTypeJpaEntity();
		entity.setId(domain.id());
		entity.setName(domain.name());
		return entity;
	}
}
