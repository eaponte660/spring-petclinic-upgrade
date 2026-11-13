package org.springframework.samples.petclinic.owners.application.port.in;

import org.springframework.samples.petclinic.owners.domain.model.Owner;

public interface UpdateOwnerUseCase {

	Owner updateOwner(UpdateOwnerCommand command);

	record UpdateOwnerCommand(
		Long id,
		String firstName,
		String lastName,
		String address,
		String city,
		String telephone
	) {}
}
