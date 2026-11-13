package org.springframework.samples.petclinic.owners.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.owners.application.port.in.CreateOwnerUseCase;
import org.springframework.samples.petclinic.owners.application.port.out.OwnerRepository;
import org.springframework.samples.petclinic.owners.domain.model.Owner;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateOwnerService implements CreateOwnerUseCase {

	private final OwnerRepository ownerRepository;

	@Override
	public Owner createOwner(CreateOwnerCommand command) {

		var newOwner = new Owner(
			null,
			command.firstName(),
			command.lastName(),
			command.address(),
			command.city(),
			command.telephone()
		);

		return ownerRepository.save(newOwner);
	}
}
