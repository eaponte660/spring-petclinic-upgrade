package org.springframework.samples.petclinic.visits.infrastructure.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.visits.application.port.in.CreateVisitUseCase;
import org.springframework.samples.petclinic.visits.application.port.in.FindVisitsByPetUseCase;
import org.springframework.samples.petclinic.visits.domain.model.Visit;
import org.springframework.samples.petclinic.visits.infrastructure.web.dto.VisitRequest;
import org.springframework.samples.petclinic.visits.infrastructure.web.dto.VisitResponse;
import org.springframework.samples.petclinic.visits.infrastructure.web.mapper.VisitWebMapper;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VisitController.class)
class VisitControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private CreateVisitUseCase createVisitUseCase;

	@MockitoBean
	private FindVisitsByPetUseCase findVisitsByPetUseCase;

	@MockitoBean
	private VisitWebMapper visitWebMapper;

	@Test
	@DisplayName("POST /api/v1/owners/{ownerId}/pets/{petId}/visits - Should create visit successfully")
	void shouldCreateVisitSuccessfully() throws Exception {
		// Arrange
		var ownerId = 1L;
		var petId = 1L;
		var today = LocalDate.now();
		var request = new VisitRequest(today, "Checkup");
		var domainVisit = new Visit(1L, today, "Checkup", petId);
		var responseDto = new VisitResponse(1L, today, "Checkup", petId);

		when(createVisitUseCase.createVisit(any(CreateVisitUseCase.CreateVisitCommand.class))).thenReturn(domainVisit);
		when(visitWebMapper.toResponse(domainVisit)).thenReturn(responseDto);

		// Act & Assert
		mockMvc.perform(post("/api/v1/owners/{ownerId}/pets/{petId}/visits", ownerId, petId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.responseCode").exists())
			.andExpect(jsonPath("$.data.id").value(1L))
			.andExpect(jsonPath("$.data.description").value("Checkup"));
	}

	@Test
	@DisplayName("GET /api/v1/owners/{ownerId}/pets/{petId}/visits - Should return visits by pet ID successfully")
	void shouldReturnVisitsByPetIdSuccessfully() throws Exception {
		// Arrange
		var ownerId = 1L;
		var petId = 1L;
		var today = LocalDate.now();
		var domainVisit = new Visit(1L, today, "Dentist", petId);
		var responseDto = new VisitResponse(1L, today, "Dentist", petId);

		when(findVisitsByPetUseCase.findVisitsByPetId(any(FindVisitsByPetUseCase.FindVisitsByPetQuery.class)))
			.thenReturn(List.of(domainVisit));
		when(visitWebMapper.toResponse(domainVisit)).thenReturn(responseDto);

		// Act & Assert
		mockMvc.perform(get("/api/v1/owners/{ownerId}/pets/{petId}/visits", ownerId, petId)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.responseCode").exists())
			.andExpect(jsonPath("$.data[0].id").value(1L))
			.andExpect(jsonPath("$.data[0].description").value("Dentist"));
	}
}
