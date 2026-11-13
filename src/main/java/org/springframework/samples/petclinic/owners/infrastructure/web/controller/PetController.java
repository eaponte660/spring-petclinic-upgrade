package org.springframework.samples.petclinic.owners.infrastructure.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.owners.application.port.in.CreatePetUseCase;
import org.springframework.samples.petclinic.owners.application.port.in.FindPetByIdUseCase;
import org.springframework.samples.petclinic.owners.application.port.in.FindPetsByOwnerUseCase;
import org.springframework.samples.petclinic.shared.domain.model.PageRequest;
import org.springframework.samples.petclinic.shared.domain.model.PageResult;
import org.springframework.samples.petclinic.owners.application.port.in.UpdatePetUseCase;
import org.springframework.samples.petclinic.shared.infrastructure.web.dto.ControllerResponse;
import org.springframework.samples.petclinic.owners.infrastructure.web.dto.PetRequest;
import org.springframework.samples.petclinic.owners.infrastructure.web.dto.PetResponse;
import org.springframework.samples.petclinic.owners.infrastructure.web.mapper.PetWebMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/owners/{ownerId}/pets")
@RequiredArgsConstructor
public class PetController {

	private final CreatePetUseCase createPetUseCase;
	private final UpdatePetUseCase updatePetUseCase;
	private final FindPetsByOwnerUseCase findPetsByOwnerUseCase;
	private final FindPetByIdUseCase findPetByIdUseCase;
	private final PetWebMapper petWebMapper;

	@PostMapping
	public ResponseEntity<ControllerResponse<PetResponse>> createPet(
		@PathVariable Long ownerId,
		@Valid @RequestBody PetRequest request
	) {
		var command = new CreatePetUseCase.CreatePetCommand(
			ownerId,
			request.name(),
			request.birthDate(),
			request.typeId()
		);
		var createdPet = createPetUseCase.createPet(command);
		var body = ControllerResponse.created(petWebMapper.toResponse(createdPet));
		return ResponseEntity.status(body.httpStatus()).body(body);
	}

	@PutMapping("/{petId}")
	public ResponseEntity<ControllerResponse<PetResponse>> updatePet(
		@PathVariable Long ownerId,
		@PathVariable Long petId,
		@Valid @RequestBody PetRequest request
	) {
		var command = new UpdatePetUseCase.UpdatePetCommand(
			petId,
			request.name(),
			request.birthDate(),
			request.typeId(),
			ownerId
		);
		var updatedPet = updatePetUseCase.updatePet(command);
		var body = ControllerResponse.success(petWebMapper.toResponse(updatedPet));
		return ResponseEntity.ok(body);
	}

	@GetMapping("/{petId}")
	public ResponseEntity<ControllerResponse<PetResponse>> findPetById(
		@PathVariable Long ownerId,
		@PathVariable Long petId
	) {
		var query = new FindPetByIdUseCase.FindPetQuery(petId, ownerId);
		var pet = findPetByIdUseCase.findPetById(query);
		var body = ControllerResponse.success(petWebMapper.toResponse(pet));
		return ResponseEntity.ok(body);
	}

	@GetMapping
	public ResponseEntity<ControllerResponse<PageResult<PetResponse>>> findPetsByOwner(
		@PathVariable Long ownerId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		var pageRequest = new PageRequest(page, size);
		var domainPage = findPetsByOwnerUseCase.findPetsByOwner(ownerId, pageRequest);

		var responsePage = new PageResult<>(
			domainPage.content().stream().map(petWebMapper::toResponse).toList(),
			domainPage.page(),
			domainPage.size(),
			domainPage.totalElements(),
			domainPage.totalPages()
		);

		var body = ControllerResponse.success(responsePage);
		return ResponseEntity.ok(body);
	}
}
