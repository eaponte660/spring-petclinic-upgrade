package org.springframework.samples.petclinic.owners.application.port.in;

import org.springframework.samples.petclinic.owners.domain.model.Pet;

import java.time.LocalDate;

public interface UpdatePetUseCase {

	Pet updatePet(UpdatePetCommand command);

	record UpdatePetCommand(
		Long petId,
		String name,
		LocalDate birthDate,
		Long typeId
	) {}
}
