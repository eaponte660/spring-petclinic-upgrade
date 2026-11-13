package org.springframework.samples.petclinic.owners.application.port.out;

import org.springframework.samples.petclinic.owners.application.port.in.PageRequest;
import org.springframework.samples.petclinic.owners.application.port.in.PageResult;
import org.springframework.samples.petclinic.owners.domain.model.Pet;

import java.util.Optional;

public interface PetRepository {

	Pet save(Pet pet);

	Optional<Pet> findById(Long id);

	boolean existsByOwnerIdAndNameIgnoreCase(Long ownerId, String name);

	boolean existsByOwnerIdAndNameIgnoreCaseAndIdNot(Long ownerId, String name, Long petId);

	PageResult<Pet> findByOwnerId(Long ownerId, PageRequest pageRequest);
}
