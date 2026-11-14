package org.springframework.samples.petclinic.owners.application.port.in;

import org.springframework.samples.petclinic.owners.domain.model.Pet;

import java.time.LocalDate;

public interface CreatePetUseCase {

	Pet createPet(CreatePetCommand command);

	record CreatePetCommand(
		Long ownerId,
		String name,
		LocalDate birthDate,
		Long typeId
	) {}
}
