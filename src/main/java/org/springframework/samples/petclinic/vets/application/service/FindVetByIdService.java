package org.springframework.samples.petclinic.vets.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.samples.petclinic.vets.application.port.in.FindVetByIdUseCase;
import org.springframework.samples.petclinic.vets.application.port.out.VetRepository;
import org.springframework.samples.petclinic.vets.domain.exception.VetNotFoundException;
import org.springframework.samples.petclinic.vets.domain.model.Vet;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindVetByIdService implements FindVetByIdUseCase {

	private final VetRepository vetRepository;

	@Override
	public Vet findVetById(FindVetQuery query) {
		log.info("Finding vet by id: {}", query.id());
		return vetRepository.findById(query.id())
			.orElseThrow(() -> {
				log.warn("Vet not found with id: {}", query.id());
				return new VetNotFoundException("Vet not found with id: " + query.id());
			});
	}
}
