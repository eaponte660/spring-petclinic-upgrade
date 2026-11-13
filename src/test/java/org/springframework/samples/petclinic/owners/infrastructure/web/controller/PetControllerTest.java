package org.springframework.samples.petclinic.owners.infrastructure.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.owners.application.port.in.CreatePetUseCase;
import org.springframework.samples.petclinic.owners.application.port.in.FindPetByIdUseCase;
import org.springframework.samples.petclinic.owners.application.port.in.FindPetsByOwnerUseCase;
import org.springframework.samples.petclinic.owners.application.port.in.PageRequest;
import org.springframework.samples.petclinic.owners.application.port.in.PageResult;
import org.springframework.samples.petclinic.owners.application.port.in.UpdatePetUseCase;
import org.springframework.samples.petclinic.owners.domain.model.Pet;
import org.springframework.samples.petclinic.owners.domain.model.PetType;
import org.springframework.samples.petclinic.owners.infrastructure.web.dto.PetRequest;
import org.springframework.samples.petclinic.owners.infrastructure.web.dto.PetResponse;
import org.springframework.samples.petclinic.owners.infrastructure.web.dto.PetTypeResponse;
import org.springframework.samples.petclinic.owners.infrastructure.web.mapper.PetWebMapper;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
class PetControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private CreatePetUseCase createPetUseCase;

	@MockitoBean
	private UpdatePetUseCase updatePetUseCase;

	@MockitoBean
	private FindPetsByOwnerUseCase findPetsByOwnerUseCase;

	@MockitoBean
	private FindPetByIdUseCase findPetByIdUseCase;

	@MockitoBean
	private PetWebMapper petWebMapper;

	private static final Long OWNER_ID = 1L;
	private static final Long PET_ID = 2L;
	private static final Long TYPE_ID = 1L;

	@Test
	@DisplayName("POST /api/v1/owners/{ownerId}/pets - Should create pet successfully")
	void shouldCreatePetSuccessfully() throws Exception {
		// Arrange
		var request = new PetRequest("Firulais", LocalDate.of(2020, 1, 1), TYPE_ID);
		var petType = new PetType(TYPE_ID, "Dog");
		var petTypeResponse = new PetTypeResponse(TYPE_ID, "Dog");
		var domainPet = new Pet(PET_ID, "Firulais", LocalDate.of(2020, 1, 1), petType, OWNER_ID);
		var responseDto = new PetResponse(PET_ID, "Firulais", LocalDate.of(2020, 1, 1), petTypeResponse, OWNER_ID);

		when(createPetUseCase.createPet(any(CreatePetUseCase.CreatePetCommand.class))).thenReturn(domainPet);
		when(petWebMapper.toResponse(domainPet)).thenReturn(responseDto);

		// Act & Assert
		mockMvc.perform(post("/api/v1/owners/{ownerId}/pets", OWNER_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.data.id").value(PET_ID))
			.andExpect(jsonPath("$.data.name").value("Firulais"));

		verify(createPetUseCase).createPet(any(CreatePetUseCase.CreatePetCommand.class));
	}

	@Test
	@DisplayName("PUT /api/v1/owners/{ownerId}/pets/{petId} - Should update pet successfully")
	void shouldUpdatePetSuccessfully() throws Exception {
		// Arrange
		var request = new PetRequest("Firulais Updated", LocalDate.of(2020, 1, 1), TYPE_ID);
		var petType = new PetType(TYPE_ID, "Dog");
		var petTypeResponse = new PetTypeResponse(TYPE_ID, "Dog");
		var domainPet = new Pet(PET_ID, "Firulais Updated", LocalDate.of(2020, 1, 1), petType, OWNER_ID);
		var responseDto = new PetResponse(PET_ID, "Firulais Updated", LocalDate.of(2020, 1, 1), petTypeResponse, OWNER_ID);

		when(updatePetUseCase.updatePet(any(UpdatePetUseCase.UpdatePetCommand.class))).thenReturn(domainPet);
		when(petWebMapper.toResponse(domainPet)).thenReturn(responseDto);

		// Act & Assert
		mockMvc.perform(put("/api/v1/owners/{ownerId}/pets/{petId}", OWNER_ID, PET_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id").value(PET_ID))
			.andExpect(jsonPath("$.data.name").value("Firulais Updated"));

		verify(updatePetUseCase).updatePet(any(UpdatePetUseCase.UpdatePetCommand.class));
	}

	@Test
	@DisplayName("GET /api/v1/owners/{ownerId}/pets/{petId} - Should find pet by ID successfully")
	void shouldFindPetByIdSuccessfully() throws Exception {
		// Arrange
		var petType = new PetType(TYPE_ID, "Dog");
		var domainPet = new Pet(PET_ID, "Firulais", LocalDate.of(2020, 1, 1), petType, OWNER_ID);
		var petTypeResponse = new PetTypeResponse(TYPE_ID, "Dog");
		var responseDto = new PetResponse(PET_ID, "Firulais", LocalDate.of(2020, 1, 1), petTypeResponse, OWNER_ID);

		when(findPetByIdUseCase.findPetById(any(FindPetByIdUseCase.FindPetQuery.class))).thenReturn(domainPet);
		when(petWebMapper.toResponse(domainPet)).thenReturn(responseDto);

		// Act & Assert
		mockMvc.perform(get("/api/v1/owners/{ownerId}/pets/{petId}", OWNER_ID, PET_ID))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id").value(PET_ID));

		verify(findPetByIdUseCase).findPetById(any(FindPetByIdUseCase.FindPetQuery.class));
	}

	@Test
	@DisplayName("GET /api/v1/owners/{ownerId}/pets - Should find pets by owner successfully")
	void shouldFindPetsByOwnerSuccessfully() throws Exception {
		// Arrange
		var petType = new PetType(TYPE_ID, "Dog");
		var domainPet = new Pet(PET_ID, "Firulais", LocalDate.of(2020, 1, 1), petType, OWNER_ID);
		var petTypeResponse = new PetTypeResponse(TYPE_ID, "Dog");
		var responseDto = new PetResponse(PET_ID, "Firulais", LocalDate.of(2020, 1, 1), petTypeResponse, OWNER_ID);
		var domainPage = new PageResult<>(List.of(domainPet), 0, 10, 1L, 1);

		when(findPetsByOwnerUseCase.findPetsByOwner(eq(OWNER_ID), any(PageRequest.class))).thenReturn(domainPage);
		when(petWebMapper.toResponse(domainPet)).thenReturn(responseDto);

		// Act & Assert
		mockMvc.perform(get("/api/v1/owners/{ownerId}/pets", OWNER_ID)
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.content").isArray())
			.andExpect(jsonPath("$.data.totalElements").value(1));

		verify(findPetsByOwnerUseCase).findPetsByOwner(eq(OWNER_ID), any(PageRequest.class));
	}

	@Test
	@DisplayName("POST /api/v1/owners/{ownerId}/pets - Should return 400 Bad Request when pet request is invalid")
	void shouldReturnBadRequestWhenPetRequestInvalid() throws Exception {
		// Arrange: Un request de mascota con nombre vacío o fecha nula
		var invalidRequest = new PetRequest("", null, null);

		// Act & Assert
		mockMvc.perform(post("/api/v1/owners/{ownerId}/pets", OWNER_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidRequest)))
			.andExpect(status().isBadRequest());

		// Verificamos que NUNCA se invoque al caso de uso
		verifyNoInteractions(createPetUseCase);
	}
}
