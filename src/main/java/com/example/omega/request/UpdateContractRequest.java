package com.example.omega.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UpdateContractRequest {

	@NotNull(message = "The buyer field is required")
	@NotEmpty(message = "The buyer cannot be empty")
	private String buyer;

	@NotNull(message = "The advance payment date field is required")
	private LocalDate advancePaymentDate;

	@NotNull(message = "The delivery deadline field is required")
	private LocalDate deliveryDeadline;

	@NotNull(message = "The status field is required")
	@NotEmpty(message = "The status field cannot be empty")
	private String status;

}

