package org.springframework.samples.petclinic.owners.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.samples.petclinic.owners.application.port.in.CreatePetUseCase;
import org.springframework.samples.petclinic.owners.application.port.out.OwnerRepository;
import org.springframework.samples.petclinic.owners.application.port.out.PetRepository;
import org.springframework.samples.petclinic.owners.application.port.out.PetTypeRepository;
import org.springframework.samples.petclinic.owners.domain.exception.DuplicatePetNameException;
import org.springframework.samples.petclinic.owners.domain.exception.OwnerNotFoundException;
import org.springframework.samples.petclinic.owners.domain.exception.PetTypeNotFoundException;
import org.springframework.samples.petclinic.owners.domain.model.Pet;
import org.springframework.samples.petclinic.owners.domain.model.PetType;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreatePetService implements CreatePetUseCase {

	private final PetRepository petRepository;
	private final OwnerRepository ownerRepository;
	private final PetTypeRepository petTypeRepository;

	@Override
	public Pet createPet(CreatePetCommand command) {
		log.info("Processing creation of new pet '{}' for owner ID: {}", command.name(), command.ownerId());

		ownerRepository.findById(command.ownerId())
			.orElseThrow(() -> {
				log.warn("Failed to create pet: Owner with ID {} not found", command.ownerId());
				return new OwnerNotFoundException("Owner not found with id: " + command.ownerId());
			});

		if (petRepository.existsByOwnerIdAndNameIgnoreCase(command.ownerId(), command.name())) {
			log.warn("Failed to create pet: A pet named '{}' already exists for owner ID: {}", command.name(), command.ownerId());
			throw new DuplicatePetNameException("A pet named '" + command.name() + "' already exists for this owner");
		}

		PetType petType = petTypeRepository.findById(command.typeId())
			.orElseThrow(() -> {
				log.warn("Failed to create pet: PetType with ID {} not found", command.typeId());
				return new PetTypeNotFoundException("Pet type not found with id: " + command.typeId());
			});

		var newPet = new Pet(
			null,
			command.name(),
			command.birthDate(),
			petType,
			command.ownerId()
		);

		newPet = petRepository.save(newPet);
		log.info("Successfully created pet with ID: {} for owner ID: {}", newPet.getId(), command.ownerId());

		return newPet;
	}
}
