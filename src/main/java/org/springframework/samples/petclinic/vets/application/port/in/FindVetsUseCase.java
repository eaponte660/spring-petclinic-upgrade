package org.springframework.samples.petclinic.vets.application.port.in;

import org.springframework.samples.petclinic.shared.domain.model.PageRequest;
import org.springframework.samples.petclinic.shared.domain.model.PageResult;
import org.springframework.samples.petclinic.vets.domain.model.Vet;

public interface FindVetsUseCase {

	PageResult<Vet> findVets(FindVetsQuery query);

	record FindVetsQuery(PageRequest pageRequest) {}
}
