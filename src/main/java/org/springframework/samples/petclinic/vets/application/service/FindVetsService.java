package org.springframework.samples.petclinic.vets.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.samples.petclinic.shared.domain.model.PageResult;
import org.springframework.samples.petclinic.vets.application.port.in.FindVetsUseCase;
import org.springframework.samples.petclinic.vets.application.port.out.VetRepository;
import org.springframework.samples.petclinic.vets.domain.model.Vet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindVetsService implements FindVetsUseCase {

	private final VetRepository vetRepository;

	@Override
	@Cacheable("vets")
	public PageResult<Vet> findVets(FindVetsQuery query) {
		log.info("Finding vets with pagination - page: {}, size: {}",
			query.pageRequest().page(), query.pageRequest().size());
		return vetRepository.findAll(query.pageRequest());
	}
}
