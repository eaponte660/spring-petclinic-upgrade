package org.springframework.samples.petclinic.owners.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.samples.petclinic.owners.application.port.in.UpdatePetUseCase;
import org.springframework.samples.petclinic.owners.application.port.out.PetRepository;
import org.springframework.samples.petclinic.owners.application.port.out.PetTypeRepository;
import org.springframework.samples.petclinic.owners.domain.exception.DuplicatePetNameException;
import org.springframework.samples.petclinic.owners.domain.exception.PetNotFoundException;
import org.springframework.samples.petclinic.owners.domain.exception.PetTypeNotFoundException;
import org.springframework.samples.petclinic.owners.domain.model.Pet;
import org.springframework.samples.petclinic.owners.domain.model.PetType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UpdatePetService implements UpdatePetUseCase {

	private final PetRepository petRepository;
	private final PetTypeRepository petTypeRepository;

	@Override
	public Pet updatePet(UpdatePetCommand command) {
		log.info("Processing update for pet ID: {}", command.petId());

		Pet existingPet = petRepository.findById(command.petId())
			.orElseThrow(() -> {
				log.warn("Failed to update pet: Pet with ID {} not found", command.petId());
				return new PetNotFoundException("Pet not found with id: " + command.petId());
			});

		if (!existingPet.getOwnerId().equals(command.ownerId())) {
			log.warn("Security/Data integrity violation: Pet ID {} does not belong to owner ID {}",
				command.petId(), command.ownerId());
			throw new PetNotFoundException("The pet does not belong to the specified owner");
		}

		boolean nameExistsForOtherPet = petRepository.existsByOwnerIdAndNameIgnoreCaseAndIdNot(
			command.ownerId(),
			command.name(),
			command.petId()
		);

		if (nameExistsForOtherPet) {
			log.warn("Failed to update pet: A pet named '{}' already exists for owner ID: {}", command.name(), existingPet.getOwnerId());
			throw new DuplicatePetNameException("A pet named '" + command.name() + "' already exists for this owner");
		}

		PetType newPetType = petTypeRepository.findById(command.typeId())
			.orElseThrow(() -> {
				log.warn("Failed to update pet: PetType with ID {} not found", command.typeId());
				return new PetTypeNotFoundException("Pet type not found with id: " + command.typeId());
			});


		existingPet.updateInfo(command.name(), command.birthDate(), newPetType);

		Pet updatedPet = petRepository.save(existingPet);
		log.info("Successfully updated pet with ID: {} for owner ID: {}", updatedPet.getId(), updatedPet.getOwnerId());

		return updatedPet;
	}
}
