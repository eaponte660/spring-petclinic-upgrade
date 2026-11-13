package org.springframework.samples.petclinic.owners.application.port.out;

import org.springframework.samples.petclinic.owners.domain.model.Owner;

import java.util.Optional;

public interface OwnerRepository {

	Owner save(Owner owner);

	Optional<Owner> findById(Long id);
}
