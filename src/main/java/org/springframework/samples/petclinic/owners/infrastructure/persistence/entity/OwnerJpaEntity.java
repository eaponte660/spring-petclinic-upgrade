package org.springframework.samples.petclinic.owners.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "owners")
public class OwnerJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "first_name", nullable = false, length = 30)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 30)
	private String lastName;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false, length = 80)
	private String city;

	@Column(nullable = false, length = 10)
	private String telephone;
}
