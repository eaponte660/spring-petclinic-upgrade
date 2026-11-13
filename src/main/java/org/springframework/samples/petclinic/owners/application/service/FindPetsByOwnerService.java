package org.springframework.samples.petclinic.owners.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.samples.petclinic.owners.application.port.in.FindPetsByOwnerUseCase;
import org.springframework.samples.petclinic.owners.application.port.in.PageResult;
import org.springframework.samples.petclinic.owners.application.port.in.PageRequest;
import org.springframework.samples.petclinic.owners.application.port.out.OwnerRepository;
import org.springframework.samples.petclinic.owners.application.port.out.PetRepository;
import org.springframework.samples.petclinic.owners.domain.exception.OwnerNotFoundException;
import org.springframework.samples.petclinic.owners.domain.model.Pet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindPetsByOwnerService implements FindPetsByOwnerUseCase {

	private final PetRepository petRepository;
	private final OwnerRepository ownerRepository;

	@Override
	public PageResult<Pet> findPetsByOwner(Long ownerId, PageRequest pageRequest) {
		log.info("Fetching paged pets for owner ID: {} with page: {} and size: {}",
			ownerId, pageRequest.page(), pageRequest.size());

		ownerRepository.findById(ownerId)
			.orElseThrow(() -> {
				log.warn("Failed to fetch pets: Owner with ID {} not found", ownerId);
				return new OwnerNotFoundException("Owner not found with id: " + ownerId);
			});

		return petRepository.findByOwnerId(ownerId, pageRequest);
	}
}
