package org.springframework.samples.petclinic.visits.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.samples.petclinic.visits.application.port.out.VisitRepository;
import org.springframework.samples.petclinic.visits.domain.model.Visit;
import org.springframework.samples.petclinic.visits.infrastructure.persistence.mapper.VisitEntityMapper;
import org.springframework.samples.petclinic.visits.infrastructure.persistence.repository.SpringDataVisitRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class VisitRepositoryAdapter implements VisitRepository {

	private final SpringDataVisitRepository springDataVisitRepository;
	private final VisitEntityMapper visitEntityMapper;

	@Override
	public Visit save(Visit visit) {
		log.debug("Saving visit for pet ID: {}", visit.petId());
		var entity = visitEntityMapper.toEntity(visit);
		var savedEntity = springDataVisitRepository.save(entity);
		return visitEntityMapper.toDomain(savedEntity);
	}

	@Override
	public List<Visit> findByPetId(Long petId) {
		log.debug("Finding visits by pet ID: {}", petId);
		return springDataVisitRepository.findByPetId(petId).stream()
			.map(visitEntityMapper::toDomain)
			.toList();
	}
}
