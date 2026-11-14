package org.springframework.samples.petclinic.owners.infrastructure.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.owners.application.port.in.FindPetTypesUseCase;
import org.springframework.samples.petclinic.owners.infrastructure.web.dto.PetTypeResponse;
import org.springframework.samples.petclinic.owners.infrastructure.web.mapper.PetTypeWebMapper;
import org.springframework.samples.petclinic.shared.infrastructure.web.dto.ControllerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pets/types")
@RequiredArgsConstructor
public class PetTypeController {

	private final FindPetTypesUseCase findPetTypesUseCase;
	private final PetTypeWebMapper petTypeWebMapper;

	@GetMapping
	public ResponseEntity<ControllerResponse<List<PetTypeResponse>>> findPetTypes()
	{
		var petTypes = findPetTypesUseCase.findAll().stream().map(petTypeWebMapper::toResponse).toList();
		var body = ControllerResponse.success(petTypes);
		return ResponseEntity.ok(body);
	}
}
