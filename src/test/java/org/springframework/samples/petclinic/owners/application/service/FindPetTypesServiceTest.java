package org.springframework.samples.petclinic.owners.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.owners.application.port.out.PetTypeRepository;
import org.springframework.samples.petclinic.owners.domain.model.PetType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindPetTypesServiceTest {

	@Mock
	private PetTypeRepository petTypeRepository;

	@InjectMocks
	private FindPetTypesService findPetTypesService;

	@Test
	void shouldReturnAllPetTypesSuccessfully() {
		// Given
		List<PetType> mockPetTypes = List.of(
			new PetType(1L, "dog"),
			new PetType(2L, "cat")
		);
		when(petTypeRepository.findAll()).thenReturn(mockPetTypes);

		// When
		List<PetType> result = findPetTypesService.findAll();

		// Then
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("dog", result.getFirst().name());
		verify(petTypeRepository, times(1)).findAll();
	}
}
