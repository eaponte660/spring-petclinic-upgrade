package org.springframework.samples.petclinic.owners.infrastructure.web.mapper;

import org.springframework.samples.petclinic.owners.domain.model.Pet;
import org.springframework.samples.petclinic.owners.infrastructure.web.dto.PetResponse;
import org.springframework.samples.petclinic.owners.infrastructure.web.dto.PetTypeResponse;
import org.springframework.stereotype.Component;

@Component
public class PetWebMapper {

	public PetResponse toResponse(Pet pet) {
		if (pet == null) {
			return null;
		}

		PetTypeResponse typeResponse = null;
		if (pet.getType() != null) {
			typeResponse = new PetTypeResponse(pet.getType().id(), pet.getType().name());
		}

		return new PetResponse(
			pet.getId(),
			pet.getName(),
			pet.getBirthDate(),
			typeResponse,
			null
		);
	}
}
