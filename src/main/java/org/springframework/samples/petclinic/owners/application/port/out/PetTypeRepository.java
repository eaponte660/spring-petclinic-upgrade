package org.springframework.samples.petclinic.owners.application.port.out;

import org.springframework.samples.petclinic.owners.domain.model.PetType;

import java.util.List;
import java.util.Optional;

public interface PetTypeRepository {

	Optional<PetType> findById(Long id);

	List<PetType> findAll();
}
