package org.springframework.samples.petclinic.vets.application.port.out;

import org.springframework.samples.petclinic.shared.domain.model.PageRequest;
import org.springframework.samples.petclinic.shared.domain.model.PageResult;
import org.springframework.samples.petclinic.vets.domain.model.Vet;

import java.util.Optional;

public interface VetRepository {

	Optional<Vet> findById(Long id);

	PageResult<Vet> findAll(PageRequest pageRequest);
}
