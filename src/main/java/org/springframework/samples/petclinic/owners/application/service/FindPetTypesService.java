package org.springframework.samples.petclinic.owners.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.samples.petclinic.owners.application.port.in.FindPetTypesUseCase;
import org.springframework.samples.petclinic.owners.application.port.out.PetTypeRepository;
import org.springframework.samples.petclinic.owners.domain.model.PetType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindPetTypesService implements FindPetTypesUseCase {

	private final PetTypeRepository petTypeRepository;

	@Override
	@Cacheable("petTypes")
	public List<PetType> findAll() {
		log.info("Fetching pet types from Database (Cache miss)");
		return petTypeRepository.findAll();
	}
}
