package org.springframework.samples.petclinic.owners.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.samples.petclinic.owners.application.port.in.PageRequest;
import org.springframework.samples.petclinic.owners.application.port.out.OwnerRepository;
import org.springframework.samples.petclinic.owners.domain.model.Owner;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.mapper.OwnerEntityMapper;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.repository.SpringDataOwnerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OwnerRepositoryAdapter implements OwnerRepository {

	private final SpringDataOwnerRepository springDataOwnerRepository;
	private final OwnerEntityMapper ownerEntityMapper;

	@Override
	public Owner save(Owner owner) {
		var entity = ownerEntityMapper.toEntity(owner);
		var savedEntity = springDataOwnerRepository.save(entity);
		return ownerEntityMapper.toDomain(savedEntity);
	}

	@Override
	public Optional<Owner> findById(Long id) {
		return springDataOwnerRepository.findById(id)
			.map(ownerEntityMapper::toDomain);
	}

	@Override
	public List<Owner> findByLastNameContainingIgnoreCase(String lastName, PageRequest sharedPageRequest) {
		// Convertimos nuestro record compartido PageRequest al Pageable de Spring Data
		Pageable pageable = org.springframework.data.domain.PageRequest
			.of(sharedPageRequest.page(), sharedPageRequest.size(), Sort.by("lastName").ascending());

		return springDataOwnerRepository.findByLastNameContainingIgnoreCase(lastName, pageable)
			.stream()
			.map(ownerEntityMapper::toDomain)
			.collect(Collectors.toList());
	}

	@Override
	public long countByLastNameContainingIgnoreCase(String lastName) {
		return springDataOwnerRepository.countByLastNameContainingIgnoreCase(lastName);
	}
}
