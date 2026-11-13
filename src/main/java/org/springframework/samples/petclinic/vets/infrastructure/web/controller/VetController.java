package org.springframework.samples.petclinic.vets.infrastructure.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.shared.domain.model.PageRequest;
import org.springframework.samples.petclinic.shared.domain.model.PageResult;
import org.springframework.samples.petclinic.shared.infrastructure.web.dto.ControllerResponse;
import org.springframework.samples.petclinic.vets.application.port.in.FindVetByIdUseCase;
import org.springframework.samples.petclinic.vets.application.port.in.FindVetsUseCase;
import org.springframework.samples.petclinic.vets.infrastructure.web.dto.VetResponse;
import org.springframework.samples.petclinic.vets.infrastructure.web.mapper.VetWebMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/vets")
@RequiredArgsConstructor
public class VetController {

	private final FindVetByIdUseCase findVetByIdUseCase;
	private final FindVetsUseCase findVetsUseCase;
	private final VetWebMapper vetWebMapper;

	@GetMapping("/{vetId}")
	public ResponseEntity<ControllerResponse<VetResponse>> findVetById(
		@PathVariable Long vetId
	) {
		log.info("REST request to find vet by id: {}", vetId);
		var query = new FindVetByIdUseCase.FindVetQuery(vetId);
		var vet = findVetByIdUseCase.findVetById(query);

		var body = ControllerResponse.success(vetWebMapper.toResponse(vet));
		return ResponseEntity.ok(body);
	}

	@GetMapping
	public ResponseEntity<ControllerResponse<PageResult<VetResponse>>> findVets(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		log.info("REST request to find vets with page: {}, size: {}", page, size);
		var query = new FindVetsUseCase.FindVetsQuery(new PageRequest(page, size));
		var domainPage = findVetsUseCase.findVets(query);

		var responsePage = new PageResult<>(
			domainPage.content().stream().map(vetWebMapper::toResponse).toList(),
			domainPage.page(),
			domainPage.size(),
			domainPage.totalElements(),
			domainPage.totalPages()
		);

		var body = ControllerResponse.success(responsePage);
		return ResponseEntity.ok(body);
	}
}
