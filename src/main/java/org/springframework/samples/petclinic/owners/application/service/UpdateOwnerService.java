package org.springframework.samples.petclinic.owners.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.samples.petclinic.owners.application.port.in.UpdateOwnerUseCase;
import org.springframework.samples.petclinic.owners.application.port.out.OwnerRepository;
import org.springframework.samples.petclinic.owners.domain.exception.OwnerNotFoundException;
import org.springframework.samples.petclinic.owners.domain.model.Owner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UpdateOwnerService implements UpdateOwnerUseCase {

	private final OwnerRepository ownerRepository;

	@Override
	public Owner updateOwner(UpdateOwnerCommand command) {
		log.info("Processing update for owner ID: {}", command.id());

		Owner owner = ownerRepository.findById(command.id())
			.orElseThrow(() -> {
				log.warn("Failed to update: Owner with ID {} not found", command.id());
				return new OwnerNotFoundException("Owner not found with id: " + command.id());
			});

		owner.updateInfo(
			command.firstName(),
			command.lastName(),
			command.address(),
			command.city(),
			command.telephone()
		);

		Owner updatedOwner = ownerRepository.save(owner);
		log.info("Successfully updated owner ID: {}", updatedOwner.getId());

		return updatedOwner;
	}
}
