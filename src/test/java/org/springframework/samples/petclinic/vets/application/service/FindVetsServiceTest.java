package org.springframework.samples.petclinic.vets.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.shared.domain.model.PageRequest;
import org.springframework.samples.petclinic.shared.domain.model.PageResult;
import org.springframework.samples.petclinic.vets.application.port.out.VetRepository;
import org.springframework.samples.petclinic.vets.domain.model.Vet;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindVetsServiceTest {

	@Mock
	private VetRepository vetRepository;

	@InjectMocks
	private FindVetsService findVetsService;

	@Test
	@DisplayName("Should return paginated list of vets successfully")
	void shouldReturnPaginatedVetsSuccessfully() {
		// Arrange
		var pageRequest = new PageRequest(0, 10);
		var vet = new Vet(1L, "Helen", "Leary", List.of());
		var pageResult = new PageResult<>(List.of(vet), 0, 10, 1L, 1);

		when(vetRepository.findAll(pageRequest)).thenReturn(pageResult);

		var query = new FindVetsService.FindVetsQuery(pageRequest);

		// Act
		var result = findVetsService.findVets(query);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.content()).hasSize(1);
		assertThat(result.totalElements()).isEqualTo(1L);
		assertThat(result.content().get(0).firstName()).isEqualTo("Helen");

		verify(vetRepository).findAll(pageRequest);
	}
}
