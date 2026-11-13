package org.springframework.samples.petclinic.owners.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.samples.petclinic.owners.application.port.in.FindPetByIdUseCase;
import org.springframework.samples.petclinic.owners.application.port.out.PetRepository;
import org.springframework.samples.petclinic.owners.domain.exception.PetNotFoundException;
import org.springframework.samples.petclinic.owners.domain.model.Pet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindPetByIdService implements FindPetByIdUseCase {

	private final PetRepository petRepository;

	@Override
	public Pet findPetById(FindPetQuery query) {
		log.info("Processing find pet by ID: {}", query.id());

		return petRepository.findById(query.id())
			.orElseThrow(() -> {
				log.warn("Failed to find pet: Pet with ID {} not found", query.id());
				return new PetNotFoundException("Pet not found with id: " + query.id());
			});
	}
}
