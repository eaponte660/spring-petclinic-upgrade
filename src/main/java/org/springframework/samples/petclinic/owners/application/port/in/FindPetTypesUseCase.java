package org.springframework.samples.petclinic.owners.application.port.in;

import org.springframework.samples.petclinic.owners.domain.model.PetType;

import java.util.List;

public interface FindPetTypesUseCase {

	List<PetType> findAll();
}
