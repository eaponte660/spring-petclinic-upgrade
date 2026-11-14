package org.springframework.samples.petclinic.owners.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.owners.application.port.in.CreateOwnerUseCase;
import org.springframework.samples.petclinic.owners.application.port.out.OwnerRepository;
import org.springframework.samples.petclinic.owners.domain.model.Owner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOwnerServiceTest {

	private static final Long OWNER_ID = 1L;
	private static final String FIRST_NAME = "John";
	private static final String LAST_NAME = "Doe";
	private static final String ADDRESS = "123 Main St";
	private static final String CITY = "Springfield";
	private static final String TELEPHONE = "1234567890";

	@Mock
	private OwnerRepository ownerRepository;

	@InjectMocks
	private CreateOwnerService createOwnerService;

	@Test
	@DisplayName("Should successfully create and return a new owner")
	void shouldCreateOwnerSuccessfully() {
		// Arrange
		var command = new CreateOwnerUseCase.CreateOwnerCommand(
			FIRST_NAME,
			LAST_NAME,
			ADDRESS,
			CITY,
			TELEPHONE
		);

		var savedOwner = new Owner(
			OWNER_ID,
			FIRST_NAME,
			LAST_NAME,
			ADDRESS,
			CITY,
			TELEPHONE,
			List.of()
		);

		when(ownerRepository.save(any(Owner.class))).thenReturn(savedOwner);

		// Act
		Owner result = createOwnerService.createOwner(command);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(OWNER_ID);
		assertThat(result.getFirstName()).isEqualTo(FIRST_NAME);
		assertThat(result.getLastName()).isEqualTo(LAST_NAME);
		assertThat(result.getAddress()).isEqualTo(ADDRESS);
		assertThat(result.getCity()).isEqualTo(CITY);
		assertThat(result.getTelephone()).isEqualTo(TELEPHONE);

		// Verify interaction
		verify(ownerRepository, times(1)).save(any(Owner.class));
	}
}
