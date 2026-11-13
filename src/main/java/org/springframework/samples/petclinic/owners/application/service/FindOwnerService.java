package org.springframework.samples.petclinic.owners.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.samples.petclinic.owners.application.port.in.FindOwnerUseCase;
import org.springframework.samples.petclinic.owners.application.port.out.OwnerRepository;
import org.springframework.samples.petclinic.owners.domain.exception.OwnerNotFoundException;
import org.springframework.samples.petclinic.owners.domain.model.Owner;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindOwnerService implements FindOwnerUseCase {

	private final OwnerRepository ownerRepository;

	@Override
	public Owner findOwnerById(FindOwnerQuery query) {
		log.info("Searching for owner with ID: {}", query.id());

		Owner owner = ownerRepository.findById(query.id())
			.orElseThrow(() -> {
				log.warn("Owner with ID: {} not found", query.id());
				return new OwnerNotFoundException("Owner not found with id: " + query.id());
			});

		log.info("Successfully found owner with ID: {}", query.id());

		return owner;
	}
}
