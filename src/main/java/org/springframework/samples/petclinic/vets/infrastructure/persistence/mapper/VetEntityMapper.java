package org.springframework.samples.petclinic.vets.infrastructure.persistence.mapper;

import org.springframework.samples.petclinic.vets.domain.model.Specialty;
import org.springframework.samples.petclinic.vets.domain.model.Vet;
import org.springframework.samples.petclinic.vets.infrastructure.persistence.entity.SpecialtyEntity;
import org.springframework.samples.petclinic.vets.infrastructure.persistence.entity.VetEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VetEntityMapper {

	public Vet toDomain(VetEntity entity) {
		if (entity == null) {
			return null;
		}
		List<Specialty> specialties = entity.getSpecialties().stream()
			.map(this::toDomainSpecialty)
			.toList();

		return new Vet(
			entity.getId(),
			entity.getFirstName(),
			entity.getLastName(),
			specialties
		);
	}

	public Specialty toDomainSpecialty(SpecialtyEntity entity) {
		if (entity == null) {
			return null;
		}
		return new Specialty(entity.getId(), entity.getName());
	}
}
