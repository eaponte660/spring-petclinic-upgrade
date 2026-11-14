package org.springframework.samples.petclinic.owners.infrastructure.persistence.mapper;

import org.springframework.samples.petclinic.owners.domain.model.Pet;
import org.springframework.samples.petclinic.owners.domain.model.PetType;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.entity.OwnerJpaEntity;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.entity.PetJpaEntity;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.entity.PetTypeJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PetEntityMapper {

	public Pet toDomain(PetJpaEntity entity) {
		if (entity == null) {
			return null;
		}

		PetType type = null;
		if (entity.getType() != null) {
			type = new PetType(entity.getType().getId(), entity.getType().getName());
		}

		Long ownerId = entity.getOwner() != null ? entity.getOwner().getId() : null;

		return new Pet(
			entity.getId(),
			entity.getName(),
			entity.getBirthDate(),
			type,
			ownerId
		);
	}

	public PetJpaEntity toEntity(Pet domain, OwnerJpaEntity ownerEntity, PetTypeJpaEntity typeEntity) {
		if (domain == null) {
			return null;
		}

		var entity = new PetJpaEntity();

		entity.setId(domain.getId());
		entity.setName(domain.getName());
		entity.setBirthDate(domain.getBirthDate());
		entity.setOwner(ownerEntity);
		entity.setType(typeEntity);

		return entity;
	}
}
