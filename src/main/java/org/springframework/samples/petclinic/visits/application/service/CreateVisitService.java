package org.springframework.samples.petclinic.visits.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.samples.petclinic.visits.application.port.in.CreateVisitUseCase;
import org.springframework.samples.petclinic.visits.application.port.out.VisitRepository;
import org.springframework.samples.petclinic.visits.domain.model.Visit;
import org.springframework.samples.petclinic.visits.domain.model.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateVisitService implements CreateVisitUseCase {

	private final VisitRepository visitRepository;

	@Override
	public Visit createVisit(CreateVisitUseCase.CreateVisitCommand command) {
		log.info("Creating visit for pet ID: {} on date: {}", command.petId(), command.date());

		var visit = new Visit(null, command.date(), command.description(), command.petId());

		try {
			return visitRepository.save(visit);
		} catch (DataIntegrityViolationException ex) {
			log.warn("Failed to create visit. Pet ID {} likely does not exist.", command.petId());
			throw new ResourceNotFoundException("Pet not found with ID: " + command.petId());
		}
	}
}
