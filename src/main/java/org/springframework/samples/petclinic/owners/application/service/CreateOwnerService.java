package org.springframework.samples.petclinic.owners.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.samples.petclinic.owners.application.port.in.CreateOwnerUseCase;
import org.springframework.samples.petclinic.owners.application.port.out.OwnerRepository;
import org.springframework.samples.petclinic.owners.domain.model.Owner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CreateOwnerService implements CreateOwnerUseCase {

	private final OwnerRepository ownerRepository;

	@Override
	public Owner createOwner(CreateOwnerCommand command) {
		log.info("Processing create new owner: {}", command.fullName());

		var newOwner = new Owner(
			null,
			command.firstName(),
			command.lastName(),
			command.address(),
			command.city(),
			command.telephone(),
			new ArrayList<>()
		);

		newOwner = ownerRepository.save(newOwner);
		log.info("Successfully created owner with ID: {}", newOwner.getId());

		return newOwner;
	}
}
