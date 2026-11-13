package org.springframework.samples.petclinic.owners.application.port.in;

import org.springframework.samples.petclinic.owners.domain.model.Pet;

public interface FindPetsByOwnerUseCase {

	PageResult<Pet> findPetsByOwner(Long ownerId, PageRequest pageRequest);
}
