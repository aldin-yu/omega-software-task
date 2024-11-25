package com.example.omega.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Item {
	private int id;

	private String name;

	private String supplier;

	private Integer quantity;

	private String status;

	private boolean deleted;
}
