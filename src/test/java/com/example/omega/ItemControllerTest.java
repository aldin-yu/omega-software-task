package com.example.omega;

import com.example.omega.model.Contract;
import com.example.omega.model.Item;
import com.example.omega.repository.ContractRepository;
import com.example.omega.request.UpdateItemRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testuser")
public class ItemControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private ObjectMapper objectMapper;

	String contractNumber = "1/2024";

	@BeforeEach
	public void setUp() {

		Item item1 = Item.builder()
			.id(1)
			.status("CREATED")
			.name("Item 1")
			.quantity(10)
			.supplier("Supplier A")
			.deleted(false)
			.build();

		Item item2 = Item.builder()
			.id(2)
			.status("CREATED")
			.name("Item 2")
			.quantity(5)
			.supplier("Supplier B")
			.deleted(false)
			.build();

		Contract contract = Contract.builder()
				.deliveryDate(LocalDate.now())
				.advancePaymentDate(LocalDate.now().minusDays(5))
				.status("CREATED")
				.buyer("John Doe")
				.items(List.of(item1, item2))
				.deleted(false).build();

		contractRepository.saveAndFlush(contract);

	}

	@Test
	void fetchItems_ShouldReturnItems() throws Exception {
		mockMvc.perform(get("/v1/api/items/fetch")
			                .param("contractNumber", contractNumber))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].name").value("Item 1"))
			.andExpect(jsonPath("$[0].supplier").value("Supplier A"))
			.andExpect(jsonPath("$[0].status").value("CREATED"))
			.andExpect(jsonPath("$[0].deleted").value(false))
			.andExpect(jsonPath("$[0].id").value(1))
			.andExpect(jsonPath("$[0].quantity").value(10))
			.andExpect(jsonPath("$[1].name").value("Item 2"))
			.andExpect(jsonPath("$[1].supplier").value("Supplier B"))
			.andExpect(jsonPath("$[1].status").value("CREATED"))
			.andExpect(jsonPath("$[1].deleted").value(false))
			.andExpect(jsonPath("$[1].id").value(2))
			.andExpect(jsonPath("$[1].quantity").value(5));

	}

	@Test
	void updateItems_ShouldUpdateItemsSuccessfully() throws Exception {
		List<UpdateItemRequest> updateRequests = List.of(
			UpdateItemRequest.builder()
				.id(1)
				.name("Updated Item 1")
				.supplier("Updated Supplier A")
				.quantity(15)
				.status("ORDERED")
				.build(),
			UpdateItemRequest.builder()
				.id(2)
				.name("Updated Item 2")
				.supplier("Updated Supplier B")
				.quantity(20)
				.status("ORDERED")
				.build()
		);

		String requestBody = objectMapper.writeValueAsString(updateRequests);

		mockMvc.perform(patch("/v1/api/items/update")
			                .param("contractNumber", contractNumber)
			                .contentType(MediaType.APPLICATION_JSON)
			                .content(requestBody))
			.andExpect(status().isOk());
	}

	@Test
	void softDeleteItems_ShouldDeleteItemsSuccessfully() throws Exception {
		List<Integer> itemIds = List.of(1, 2);

		String requestBody = objectMapper.writeValueAsString(itemIds);

		mockMvc.perform(delete("/v1/api/items/delete")
			                .param("contractNumber", contractNumber)
			                .contentType(MediaType.APPLICATION_JSON)
			                .content(requestBody))
			.andExpect(status().isOk());
	}

}
