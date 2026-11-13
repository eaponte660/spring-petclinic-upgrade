package org.springframework.samples.petclinic.owners.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.samples.petclinic.shared.domain.model.PageRequest;
import org.springframework.samples.petclinic.shared.domain.model.PageResult;
import org.springframework.samples.petclinic.owners.application.port.out.PetRepository;
import org.springframework.samples.petclinic.owners.domain.model.Pet;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.mapper.PetEntityMapper;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.repository.SpringDataOwnerRepository;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.repository.SpringDataPetRepository;
import org.springframework.samples.petclinic.owners.infrastructure.persistence.repository.SpringDataPetTypeRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PetRepositoryAdapter implements PetRepository {

	private final SpringDataPetRepository springDataPetRepository;
	private final SpringDataOwnerRepository springDataOwnerRepository;
	private final SpringDataPetTypeRepository springDataPetTypeRepository;
	private final PetEntityMapper petEntityMapper;

	@Override
	public Pet save(Pet pet) {
		var ownerEntity = springDataOwnerRepository.findById(pet.getOwnerId())
			.orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + pet.getOwnerId()));

		var typeEntity = springDataPetTypeRepository.findById(pet.getType().id())
			.orElseThrow(() -> new IllegalArgumentException("Pet type not found with id: " + pet.getType().id()));

		var entity = petEntityMapper.toEntity(pet, ownerEntity, typeEntity);
		var savedEntity = springDataPetRepository.save(entity);

		return petEntityMapper.toDomain(savedEntity);
	}

	@Override
	public Optional<Pet> findById(Long id) {
		return springDataPetRepository.findById(id)
			.map(petEntityMapper::toDomain);
	}

	@Override
	public PageResult<Pet> findByOwnerId(Long ownerId, PageRequest sharedPageRequest) {
		Pageable pageable = org.springframework.data.domain.PageRequest
			.of(sharedPageRequest.page(), sharedPageRequest.size(), Sort.by("name").ascending());

		var pageResult = springDataPetRepository.findByOwnerId(ownerId, pageable);

		var pets = pageResult.getContent().stream()
			.map(petEntityMapper::toDomain)
			.toList();

		return new PageResult<>(
			pets,
			pageResult.getNumber(),
			pageResult.getSize(),
			pageResult.getTotalElements(),
			pageResult.getTotalPages()
		);
	}

	@Override
	public boolean existsByOwnerIdAndNameIgnoreCase(Long ownerId, String name) {
		return springDataPetRepository.existsByOwnerIdAndNameIgnoreCase(ownerId, name);
	}

	@Override
	public boolean existsByOwnerIdAndNameIgnoreCaseAndIdNot(Long ownerId, String name, Long id) {
		return springDataPetRepository.existsByOwnerIdAndNameIgnoreCaseAndIdNot(ownerId, name, id);
	}
}
