package org.springframework.samples.petclinic.owners.application.port.in;

import org.springframework.samples.petclinic.owners.domain.model.Owner;

public interface CreateOwnerUseCase {

	Owner createOwner(CreateOwnerCommand command);

	record CreateOwnerCommand(
		String firstName,
		String lastName,
		String address,
		String city,
		String telephone
	) {}
}
