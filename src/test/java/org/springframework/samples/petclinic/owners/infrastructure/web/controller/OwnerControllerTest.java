package org.springframework.samples.petclinic.owners.infrastructure.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.owners.application.port.in.CreateOwnerUseCase;
import org.springframework.samples.petclinic.owners.application.port.in.FindOwnerUseCase;
import org.springframework.samples.petclinic.shared.domain.model.PageResult;
import org.springframework.samples.petclinic.owners.application.port.in.SearchOwnersUseCase;
import org.springframework.samples.petclinic.owners.application.port.in.UpdateOwnerUseCase;
import org.springframework.samples.petclinic.owners.domain.model.Owner;
import org.springframework.samples.petclinic.owners.infrastructure.web.dto.OwnerRequest;
import org.springframework.samples.petclinic.owners.infrastructure.web.dto.OwnerResponse;
import org.springframework.samples.petclinic.owners.infrastructure.web.mapper.OwnerWebMapper;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OwnerController.class)
class OwnerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private SearchOwnersUseCase searchOwnersUseCase;

	@MockitoBean
	private FindOwnerUseCase findOwnerUseCase;

	@MockitoBean
	private CreateOwnerUseCase createOwnerUseCase;

	@MockitoBean
	private UpdateOwnerUseCase updateOwnerUseCase;

	@MockitoBean
	private OwnerWebMapper ownerWebMapper;

	private static final Long OWNER_ID = 1L;

	@Test
	@DisplayName("POST /api/v1/owners - Should create owner successfully")
	void shouldCreateOwnerSuccessfully() throws Exception {
		// Arrange
		var request = new OwnerRequest("John", "Doe", "123 Main St", "Springfield", "1234567890");
		var domainOwner = new Owner(OWNER_ID, "John", "Doe", "123 Main St", "Springfield", "1234567890", List.of());
		var responseDto = new OwnerResponse(OWNER_ID, "John", "Doe", "123 Main St", "Springfield", "1234567890", List.of());

		when(createOwnerUseCase.createOwner(any(CreateOwnerUseCase.CreateOwnerCommand.class))).thenReturn(domainOwner);
		when(ownerWebMapper.toResponse(domainOwner)).thenReturn(responseDto);

		// Act & Assert
		mockMvc.perform(post("/api/v1/owners")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.data.id").value(OWNER_ID))
			.andExpect(jsonPath("$.data.firstName").value("John"));

		verify(createOwnerUseCase).createOwner(any(CreateOwnerUseCase.CreateOwnerCommand.class));
	}

	@Test
	@DisplayName("PUT /api/v1/owners/{ownerId} - Should update owner successfully")
	void shouldUpdateOwnerSuccessfully() throws Exception {
		// Arrange
		var request = new OwnerRequest("John", "Doe", "123 Main St", "Springfield", "1234567890");
		var domainOwner = new Owner(OWNER_ID, "John", "Doe", "123 Main St", "Springfield", "1234567890", List.of());
		var responseDto = new OwnerResponse(OWNER_ID, "John", "Doe", "123 Main St", "Springfield", "1234567890", List.of());

		when(updateOwnerUseCase.updateOwner(any(UpdateOwnerUseCase.UpdateOwnerCommand.class))).thenReturn(domainOwner);
		when(ownerWebMapper.toResponse(domainOwner)).thenReturn(responseDto);

		// Act & Assert
		mockMvc.perform(put("/api/v1/owners/{ownerId}", OWNER_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id").value(OWNER_ID))
			.andExpect(jsonPath("$.data.lastName").value("Doe"));

		verify(updateOwnerUseCase).updateOwner(any(UpdateOwnerUseCase.UpdateOwnerCommand.class));
	}

	@Test
	@DisplayName("GET /api/v1/owners/{ownerId} - Should find owner by ID successfully")
	void shouldFindOwnerByIdSuccessfully() throws Exception {
		// Arrange
		var domainOwner = new Owner(OWNER_ID, "John", "Doe", "123 Main St", "Springfield", "1234567890", List.of());
		var responseDto = new OwnerResponse(OWNER_ID, "John", "Doe", "123 Main St", "Springfield", "1234567890", List.of());

		when(findOwnerUseCase.findOwnerById(any(FindOwnerUseCase.FindOwnerQuery.class))).thenReturn(domainOwner);
		when(ownerWebMapper.toResponse(domainOwner)).thenReturn(responseDto);

		// Act & Assert
		mockMvc.perform(get("/api/v1/owners/{ownerId}", OWNER_ID))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id").value(OWNER_ID));

		verify(findOwnerUseCase).findOwnerById(any(FindOwnerUseCase.FindOwnerQuery.class));
	}

	@Test
	@DisplayName("GET /api/v1/owners - Should search owners with pagination successfully")
	void shouldSearchOwnersSuccessfully() throws Exception {
		// Arrange
		var domainOwner = new Owner(OWNER_ID, "John", "Doe", "123 Main St", "Springfield", "1234567890", List.of());
		var responseDto = new OwnerResponse(OWNER_ID, "John", "Doe", "123 Main St", "Springfield", "1234567890", List.of());
		var domainPage = new PageResult<>(List.of(domainOwner), 0, 10, 1L, 1);

		when(searchOwnersUseCase.searchOwners(any(SearchOwnersUseCase.SearchOwnersQuery.class))).thenReturn(domainPage);
		when(ownerWebMapper.toResponse(domainOwner)).thenReturn(responseDto);

		// Act & Assert
		mockMvc.perform(get("/api/v1/owners")
				.param("lastName", "Doe")
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.content").isArray())
			.andExpect(jsonPath("$.data.totalElements").value(1));

		verify(searchOwnersUseCase).searchOwners(any(SearchOwnersUseCase.SearchOwnersQuery.class));
	}

	@Test
	@DisplayName("POST /api/v1/owners - Should return 400 Bad Request when request is invalid")
	void shouldReturnBadRequestWhenOwnerRequestInvalid() throws Exception {
		// Arrange: Creamos un request con campos vacíos o nulos que violen las anotaciones @NotBlank / @NotNull
		var invalidRequest = new OwnerRequest("", "", "", "", "");

		// Act & Assert
		mockMvc.perform(post("/api/v1/owners")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidRequest)))
			.andExpect(status().isBadRequest());

		// Verificamos que NUNCA se invoque al caso de uso si la validación falla
		verifyNoInteractions(createOwnerUseCase);
	}
}
