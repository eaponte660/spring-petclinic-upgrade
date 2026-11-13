package org.springframework.samples.petclinic.vets.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.samples.petclinic.shared.domain.model.PageResult;
import org.springframework.samples.petclinic.vets.application.port.in.FindVetsUseCase;
import org.springframework.samples.petclinic.vets.application.port.out.VetRepository;
import org.springframework.samples.petclinic.vets.domain.model.Vet;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindVetsService implements FindVetsUseCase {

	private final VetRepository vetRepository;

	@Override
	public PageResult<Vet> findVets(FindVetsQuery query) {
		log.info("Finding vets with pagination - page: {}, size: {}",
			query.pageRequest().page(), query.pageRequest().size());
		return vetRepository.findAll(query.pageRequest());
	}
}
