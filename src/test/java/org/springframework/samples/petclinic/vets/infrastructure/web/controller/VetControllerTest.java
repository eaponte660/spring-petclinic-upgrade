package org.springframework.samples.petclinic.vets.infrastructure.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.shared.domain.model.PageResult;
import org.springframework.samples.petclinic.vets.application.port.in.FindVetByIdUseCase;
import org.springframework.samples.petclinic.vets.application.port.in.FindVetsUseCase;
import org.springframework.samples.petclinic.vets.domain.model.Specialty;
import org.springframework.samples.petclinic.vets.domain.model.Vet;
import org.springframework.samples.petclinic.vets.infrastructure.web.dto.SpecialtyResponse;
import org.springframework.samples.petclinic.vets.infrastructure.web.dto.VetResponse;
import org.springframework.samples.petclinic.vets.infrastructure.web.mapper.VetWebMapper;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VetController.class)
class VetControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private FindVetByIdUseCase findVetByIdUseCase;

	@MockitoBean
	private FindVetsUseCase findVetsUseCase;

	@MockitoBean
	private VetWebMapper vetWebMapper;

	@Test
	@DisplayName("GET /api/v1/vets/{vetId} - Should return vet by ID successfully")
	void shouldReturnVetByIdSuccessfully() throws Exception {
		// Arrange
		var vetId = 1L;
		var domainVet = new Vet(vetId, "James", "Carter", List.of(new Specialty(1L, "radiology")));
		var vetResponse = new VetResponse(vetId, "James", "Carter", List.of(new SpecialtyResponse(1L, "radiology")));

		when(findVetByIdUseCase.findVetById(any(FindVetByIdUseCase.FindVetQuery.class))).thenReturn(domainVet);
		when(vetWebMapper.toResponse(domainVet)).thenReturn(vetResponse);

		// Act & Assert
		mockMvc.perform(get("/api/v1/vets/{vetId}", vetId)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.responseCode").exists())
			.andExpect(jsonPath("$.data.id").value(vetId))
			.andExpect(jsonPath("$.data.firstName").value("James"))
			.andExpect(jsonPath("$.data.lastName").value("Carter"))
			.andExpect(jsonPath("$.data.specialties[0].name").value("radiology"));
	}

	@Test
	@DisplayName("GET /api/v1/vets - Should return paginated list of vets successfully")
	void shouldReturnPaginatedVetsSuccessfully() throws Exception {
		// Arrange
		var domainVet = new Vet(1L, "Helen", "Leary", List.of());
		var vetResponse = new VetResponse(1L, "Helen", "Leary", List.of());
		var domainPage = new PageResult<>(List.of(domainVet), 0, 10, 1L, 1);

		when(findVetsUseCase.findVets(any(FindVetsUseCase.FindVetsQuery.class))).thenReturn(domainPage);
		when(vetWebMapper.toResponse(domainVet)).thenReturn(vetResponse);

		// Act & Assert
		mockMvc.perform(get("/api/v1/vets")
				.param("page", "0")
				.param("size", "10")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.responseCode").exists())
			.andExpect(jsonPath("$.data.content[0].id").value(1L))
			.andExpect(jsonPath("$.data.content[0].firstName").value("Helen"))
			.andExpect(jsonPath("$.data.totalElements").value(1));
	}
}
