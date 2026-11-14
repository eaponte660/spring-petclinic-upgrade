package org.springframework.samples.petclinic.vets.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.samples.petclinic.shared.domain.model.PageRequest;
import org.springframework.samples.petclinic.shared.domain.model.PageResult;
import org.springframework.samples.petclinic.vets.application.port.out.VetRepository;
import org.springframework.samples.petclinic.vets.domain.model.Vet;
import org.springframework.samples.petclinic.vets.infrastructure.persistence.mapper.VetEntityMapper;
import org.springframework.samples.petclinic.vets.infrastructure.persistence.repository.SpringDataVetRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class VetRepositoryAdapter implements VetRepository {

	private final SpringDataVetRepository springDataVetRepository;
	private final VetEntityMapper vetEntityMapper;

	@Override
	public Optional<Vet> findById(Long id) {
		log.debug("Finding vet entity by id: {}", id);
		return springDataVetRepository.findById(id)
			.map(vetEntityMapper::toDomain);
	}

	@Override
	public PageResult<Vet> findAll(PageRequest pageRequest) {
		log.debug("Finding all vets with page: {}, size: {}", pageRequest.page(), pageRequest.size());
		var springPageable = org.springframework.data.domain.PageRequest.of(pageRequest.page(), pageRequest.size());
		var page = springDataVetRepository.findAll(springPageable);

		var content = page.getContent().stream()
			.map(vetEntityMapper::toDomain)
			.toList();

		return new PageResult<>(
			content,
			page.getNumber(),
			page.getSize(),
			page.getTotalElements(),
			page.getTotalPages()
		);
	}
}
