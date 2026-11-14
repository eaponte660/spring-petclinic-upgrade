package org.springframework.samples.petclinic.vets.infrastructure.web.mapper;

import org.springframework.samples.petclinic.vets.domain.model.Specialty;
import org.springframework.samples.petclinic.vets.domain.model.Vet;
import org.springframework.samples.petclinic.vets.infrastructure.web.dto.SpecialtyResponse;
import org.springframework.samples.petclinic.vets.infrastructure.web.dto.VetResponse;
import org.springframework.stereotype.Component;

@Component
public class VetWebMapper {

	public VetResponse toResponse(Vet vet) {
		if (vet == null) {
			return null;
		}
		var specialties = vet.specialties().stream()
			.map(this::toSpecialtyResponse)
			.toList();

		return new VetResponse(
			vet.id(),
			vet.firstName(),
			vet.lastName(),
			specialties
		);
	}

	public SpecialtyResponse toSpecialtyResponse(Specialty specialty) {
		if (specialty == null) {
			return null;
		}
		return new SpecialtyResponse(specialty.id(), specialty.name());
	}
}
