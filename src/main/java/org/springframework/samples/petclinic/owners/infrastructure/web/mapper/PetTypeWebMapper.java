package org.springframework.samples.petclinic.owners.infrastructure.web.mapper;

import org.springframework.samples.petclinic.owners.domain.model.PetType;
import org.springframework.samples.petclinic.owners.infrastructure.web.dto.PetTypeResponse;
import org.springframework.stereotype.Component;

@Component
public class PetTypeWebMapper {

	public PetTypeResponse toResponse(PetType petType) {
		if (petType == null) {
			return null;
		}

		return new PetTypeResponse(
			petType.id(),
			petType.name()
		);
	}
}
