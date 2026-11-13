package org.springframework.samples.petclinic.owners.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.samples.petclinic.owners.application.port.out.PetTypeRepository;
import org.springframework.samples.petclinic.owners.domain.model.PetType;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.mapper.PetTypeEntityMapper;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.repository.SpringDataPetTypeRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PetTypeRepositoryAdapter implements PetTypeRepository {

	private final SpringDataPetTypeRepository springDataPetTypeRepository;
	private final PetTypeEntityMapper petTypeEntityMapper;

	@Override
	public Optional<PetType> findById(Long id) {
		log.debug("Finding pet type by ID: {}", id);
		return springDataPetTypeRepository.findById(id)
			.map(petTypeEntityMapper::toDomain);
	}
}
