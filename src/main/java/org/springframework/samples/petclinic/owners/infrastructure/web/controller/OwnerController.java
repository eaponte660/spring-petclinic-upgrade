package org.springframework.samples.petclinic.owners.infrastructure.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.owners.application.port.in.CreateOwnerUseCase;
import org.springframework.samples.petclinic.owners.application.port.in.FindOwnerUseCase;
import org.springframework.samples.petclinic.owners.application.port.in.PageRequest;
import org.springframework.samples.petclinic.owners.application.port.in.PageResult;
import org.springframework.samples.petclinic.owners.application.port.in.SearchOwnersUseCase;
import org.springframework.samples.petclinic.owners.application.port.in.UpdateOwnerUseCase;
import org.springframework.samples.petclinic.owners.infrastructure.web.dto.ControllerResponse;
import org.springframework.samples.petclinic.owners.infrastructure.web.dto.OwnerRequest;
import org.springframework.samples.petclinic.owners.infrastructure.web.dto.OwnerResponse;
import org.springframework.samples.petclinic.owners.infrastructure.web.mapper.OwnerWebMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/owners")
@RequiredArgsConstructor
public class OwnerController {

	private final SearchOwnersUseCase searchOwnersUseCase;
	private final FindOwnerUseCase findOwnerUseCase;
	private final CreateOwnerUseCase createOwnerUseCase;
	private final UpdateOwnerUseCase updateOwnerUseCase;
	private final OwnerWebMapper ownerWebMapper;

	@PostMapping
	public ResponseEntity<ControllerResponse<OwnerResponse>> createOwner(
		@Valid @RequestBody OwnerRequest request
	) {
		var command = new CreateOwnerUseCase.CreateOwnerCommand(
			request.firstName(),
			request.lastName(),
			request.address(),
			request.city(),
			request.telephone()
		);
		var createdOwner = createOwnerUseCase.createOwner(command);
		var body = ControllerResponse.created(ownerWebMapper.toResponse(createdOwner));

		return ResponseEntity.status(body.httpStatus()).body(body);
	}

	@PutMapping("/{ownerId}")
	public ResponseEntity<ControllerResponse<OwnerResponse>> updateOwner(
		@PathVariable Long ownerId,
		@Valid @RequestBody OwnerRequest request
	) {
		var command = new UpdateOwnerUseCase.UpdateOwnerCommand(
			ownerId,
			request.firstName(),
			request.lastName(),
			request.address(),
			request.city(),
			request.telephone()
		);
		var updatedOwner = updateOwnerUseCase.updateOwner(command);
		var body = ControllerResponse.success(ownerWebMapper.toResponse(updatedOwner));

		return ResponseEntity.ok(body);
	}

	@GetMapping("/{ownerId}")
	public ResponseEntity<ControllerResponse<OwnerResponse>> findOwnerById(
		@PathVariable Long ownerId
	) {
		var query = new FindOwnerUseCase.FindOwnerQuery(ownerId);
		var owner = findOwnerUseCase.findOwnerById(query);

		var body = ControllerResponse.success(ownerWebMapper.toResponse(owner));
		return ResponseEntity.ok(body);
	}

	@GetMapping
	public ResponseEntity<ControllerResponse<PageResult<OwnerResponse>>> findOwners(
		@RequestParam(required = false, defaultValue = "") String lastName,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		var query = new SearchOwnersUseCase.SearchOwnersQuery(lastName, new PageRequest(page, size));
		var domainPage = searchOwnersUseCase.searchOwners(query);

		var responsePage = new PageResult<>(
			domainPage.content().stream().map(ownerWebMapper::toResponse).toList(),
			domainPage.page(),
			domainPage.size(),
			domainPage.totalElements(),
			domainPage.totalPages()
		);

		var body = ControllerResponse.success(responsePage);
		return ResponseEntity.status(body.httpStatus()).body(body);
	}
}
