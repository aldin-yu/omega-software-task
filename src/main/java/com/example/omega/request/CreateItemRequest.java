package com.example.omega.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateItemRequest {

	@NotNull(message = "Item name is a required field")
	@NotBlank(message = "Item name cannot be blank")
	private String name;

	@NotNull(message = "Supplier is a required field")
	@NotBlank(message = "Supplier cannot be blank")
	private String supplier;

	@NotNull(message = "Quantity is a required field")
	@Min(value = 1, message = "The contract must have at least one item")
	private Integer quantity;
}
