package com.example.omega.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class CreateContractRequest {
	@NotNull(message = "The buyer field is required")
	@NotEmpty(message = "The buyer cannot be empty")
	private String buyer;

	@NotNull(message = "The advance payment date field is required")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate advancePaymentDate;

	@NotNull(message = "The delivery deadline field is required")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate deliveryDeadline;

	@NotNull(message = "Items are required")
	@Size(min = 1, message = "There must be at least one item")
	@Valid
	private List<CreateItemRequest> items;
}
