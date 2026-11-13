package org.springframework.samples.petclinic.visits.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.samples.petclinic.visits.application.port.in.FindVisitsByPetUseCase;
import org.springframework.samples.petclinic.visits.application.port.out.VisitRepository;
import org.springframework.samples.petclinic.visits.domain.model.Visit;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindVisitsByPetService implements FindVisitsByPetUseCase {

	private final VisitRepository visitRepository;

	@Override
	public List<Visit> findVisitsByPetId(FindVisitsByPetQuery query) {
		log.info("Finding visits for pet ID: {}", query.petId());
		return visitRepository.findByPetId(query.petId());
	}
}
