package org.springframework.samples.petclinic.owners.infrastructure.persistence.mapper;

import lombok.AllArgsConstructor;
import org.springframework.samples.petclinic.owners.domain.model.Owner;
import org.springframework.samples.petclinic.owners.domain.model.Pet;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.entity.OwnerJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class OwnerEntityMapper {

	private final PetEntityMapper petEntityMapper;

	public Owner toDomain(OwnerJpaEntity entity) {
		if (entity == null) {
			return null;
		}

		List<Pet> domainPets = entity.getPets() != null
			? entity.getPets().stream().map(petEntityMapper::toDomain).toList()
			: List.of();

		return new Owner(
			entity.getId(),
			entity.getFirstName(),
			entity.getLastName(),
			entity.getAddress(),
			entity.getCity(),
			entity.getTelephone(),
			domainPets
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
