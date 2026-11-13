package org.springframework.samples.petclinic.owners.infrastructure.persistence.mapper;

import org.springframework.samples.petclinic.owners.domain.model.Owner;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.entity.OwnerJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class OwnerEntityMapper {

	public Owner toDomain(OwnerJpaEntity entity) {
		if (entity == null) {
			return null;
		}
		return new Owner(
			entity.getId(),
			entity.getFirstName(),
			entity.getLastName(),
			entity.getAddress(),
			entity.getCity(),
			entity.getTelephone()
		);
	}

	public OwnerJpaEntity toEntity(Owner domain) {
		if (domain == null) {
			return null;
		}

		var entity = new OwnerJpaEntity();

		entity.setId(domain.getId());
		entity.setFirstName(domain.getFirstName());
		entity.setLastName(domain.getLastName());
		entity.setAddress(domain.getAddress());
		entity.setCity(domain.getCity());
		entity.setTelephone(domain.getTelephone());

		return entity;
	}
}
