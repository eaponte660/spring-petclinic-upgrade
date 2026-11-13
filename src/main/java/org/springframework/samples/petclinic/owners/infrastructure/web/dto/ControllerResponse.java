package org.springframework.samples.petclinic.owners.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ControllerResponse<T>(
	Integer responseCode,
	String message,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime dateTime,
	T data,
	@JsonIgnore
	HttpStatus httpStatus
) {
	public static <T> ControllerResponse<T> success(T data) {
		return new ControllerResponse<>(
			0,
			"Operation successful",
			LocalDateTime.now(),
			data,
			HttpStatus.OK
		);
	}

	public static <T> ControllerResponse<T> created(T data) {
		return new ControllerResponse<>(
			0,
			"Resource created successfully",
			LocalDateTime.now(),
			data,
			HttpStatus.CREATED
		);
	}
}
