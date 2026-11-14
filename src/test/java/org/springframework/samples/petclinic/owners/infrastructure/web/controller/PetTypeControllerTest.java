package org.springframework.samples.petclinic.owners.infrastructure.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.owners.application.port.in.FindPetTypesUseCase;
import org.springframework.samples.petclinic.owners.domain.model.PetType;
import org.springframework.samples.petclinic.owners.infrastructure.web.dto.PetTypeResponse;
import org.springframework.samples.petclinic.owners.infrastructure.web.mapper.PetTypeWebMapper;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetTypeController.class)
class PetTypeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private FindPetTypesUseCase findPetTypesUseCase;

	@MockitoBean
	private PetTypeWebMapper petTypeWebMapper;

	@Test
	void shouldReturnOkWithPetTypesList() throws Exception {
		// Given
		PetType petType = new PetType(1L, "bird");
		PetTypeResponse responseDto = new PetTypeResponse(1L, "bird");

		when(findPetTypesUseCase.findAll()).thenReturn(List.of(petType));
		when(petTypeWebMapper.toResponse(petType)).thenReturn(responseDto);

		// When & Then
		mockMvc.perform(get("/api/v1/pets/types")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.responseCode").value(0))
			.andExpect(jsonPath("$.message").value("Operation successful"))
			.andExpect(jsonPath("$.data[0].id").value(1))
			.andExpect(jsonPath("$.data[0].name").value("bird"));
	}
}
