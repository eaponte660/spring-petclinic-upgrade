package org.springframework.samples.petclinic.vets.application.port.in;

import org.springframework.samples.petclinic.vets.domain.model.Vet;

public interface FindVetByIdUseCase {

	Vet findVetById(FindVetQuery query);

	record FindVetQuery(Long id) {}
}
