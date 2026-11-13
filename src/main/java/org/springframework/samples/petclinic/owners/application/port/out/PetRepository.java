package org.springframework.samples.petclinic.owners.application.port.out;

import org.springframework.samples.petclinic.owners.domain.model.Pet;

public interface PetRepository {

	Pet save(Pet pet);

	boolean existsByOwnerIdAndNameIgnoreCase(Long ownerId, String name);
}
