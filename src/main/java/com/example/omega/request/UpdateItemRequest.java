package com.example.omega.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateItemRequest {

	@NotNull(message = "The field 'id' is required")
	private int id;

	@NotNull(message = "The field 'name' is required")
	@NotEmpty(message = "The field 'name' cannot be empty")
	private String name;

	@NotNull(message = "The field 'supplier' is required")
	@NotEmpty(message = "The field 'supplier' cannot be empty")
	private String supplier;

	@NotNull(message = "The field 'quantity' is required")
	private int quantity;

	@NotNull(message = "The field 'status' is required")
	@NotEmpty(message = "The field 'status' cannot be empty")
	private String status;
}

