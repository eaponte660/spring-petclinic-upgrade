package org.springframework.samples.petclinic.owners.application.port.out;

import org.springframework.samples.petclinic.shared.domain.model.PageRequest;
import org.springframework.samples.petclinic.owners.domain.model.Owner;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository {

	Owner save(Owner owner);

	Optional<Owner> findById(Long id);

	List<Owner> findByLastNameContainingIgnoreCase(String lastName, PageRequest pageRequest);

	long countByLastNameContainingIgnoreCase(String lastName);
}
