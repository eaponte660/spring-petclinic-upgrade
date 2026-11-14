package org.springframework.samples.petclinic.owners.application.port.in;

import org.springframework.samples.petclinic.owners.domain.model.Pet;
import org.springframework.samples.petclinic.shared.domain.model.PageRequest;
import org.springframework.samples.petclinic.shared.domain.model.PageResult;

public interface FindPetsByOwnerUseCase {

	PageResult<Pet> findPetsByOwner(Long ownerId, PageRequest pageRequest);
}
