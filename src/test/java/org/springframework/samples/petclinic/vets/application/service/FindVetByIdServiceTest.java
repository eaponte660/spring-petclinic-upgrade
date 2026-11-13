package org.springframework.samples.petclinic.vets.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.vets.application.port.out.VetRepository;
import org.springframework.samples.petclinic.vets.domain.exception.VetNotFoundException;
import org.springframework.samples.petclinic.vets.domain.model.Specialty;
import org.springframework.samples.petclinic.vets.domain.model.Vet;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindVetByIdServiceTest {

	@Mock
	private VetRepository vetRepository;

	@InjectMocks
	private FindVetByIdService findVetByIdService;

	@Test
	@DisplayName("Should return vet when found by ID")
	void shouldReturnVetWhenFoundById() {
		// Arrange
		var vetId = 1L;
		var specialty = new Specialty(1L, "radiology");
		var expectedVet = new Vet(vetId, "James", "Carter", List.of(specialty));

		when(vetRepository.findById(vetId)).thenReturn(Optional.of(expectedVet));

		var query = new FindVetByIdService.FindVetQuery(vetId);

		// Act
		var result = findVetByIdService.findVetById(query);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.id()).isEqualTo(vetId);
		assertThat(result.firstName()).isEqualTo("James");
		assertThat(result.specialties()).hasSize(1);

		verify(vetRepository).findById(vetId);
	}

	@Test
	@DisplayName("Should throw VetNotFoundException when vet is not found by ID")
	void shouldThrowExceptionWhenVetNotFound() {
		// Arrange
		var vetId = 99L;
		when(vetRepository.findById(vetId)).thenReturn(Optional.empty());

		var query = new FindVetByIdService.FindVetQuery(vetId);

		// Act & Assert
		assertThatThrownBy(() -> findVetByIdService.findVetById(query))
			.isInstanceOf(VetNotFoundException.class)
			.hasMessageContaining("Vet not found with id: " + vetId);

		verify(vetRepository).findById(vetId);
	}
}
