package org.springframework.samples.petclinic.visits.infrastructure.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.shared.infrastructure.web.dto.ControllerResponse;
import org.springframework.samples.petclinic.visits.application.port.in.CreateVisitUseCase;
import org.springframework.samples.petclinic.visits.application.port.in.FindVisitsByPetUseCase;
import org.springframework.samples.petclinic.visits.infrastructure.web.dto.VisitRequest;
import org.springframework.samples.petclinic.visits.infrastructure.web.dto.VisitResponse;
import org.springframework.samples.petclinic.visits.infrastructure.web.mapper.VisitWebMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/owners/{ownerId}/pets/{petId}/visits")
@RequiredArgsConstructor
public class VisitController {

	private final CreateVisitUseCase createVisitUseCase;
	private final FindVisitsByPetUseCase findVisitsByPetUseCase;
	private final VisitWebMapper visitWebMapper;

	@PostMapping
	public ResponseEntity<ControllerResponse<VisitResponse>> createVisit(
		@PathVariable Long ownerId,
		@PathVariable Long petId,
		@Valid @RequestBody VisitRequest request
	) {
		log.info("REST request to create visit for pet ID: {} under owner ID: {}", petId, ownerId);
		var command = new CreateVisitUseCase.CreateVisitCommand(
			request.date(),
			request.description(),
			petId
		);
		var visit = createVisitUseCase.createVisit(command);
		var response = ControllerResponse.success(visitWebMapper.toResponse(visit));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<ControllerResponse<List<VisitResponse>>> findVisitsByPet(
		@PathVariable Long ownerId,
		@PathVariable Long petId
	) {
		log.info("REST request to find visits for pet ID: {} under owner ID: {}", petId, ownerId);
		var query = new FindVisitsByPetUseCase.FindVisitsByPetQuery(petId);
		var visits = findVisitsByPetUseCase.findVisitsByPetId(query);

		var responseList = visits.stream()
			.map(visitWebMapper::toResponse)
			.toList();

		var response = ControllerResponse.success(responseList);
		return ResponseEntity.ok(response);
	}
}
