package org.springframework.samples.petclinic.owners.application.port.in;

import org.springframework.samples.petclinic.owners.domain.model.Pet;

public interface FindPetByIdUseCase {

	Pet findPetById(FindPetQuery query);

	record FindPetQuery(
		Long id,
		Long ownerId
	) {}
}
