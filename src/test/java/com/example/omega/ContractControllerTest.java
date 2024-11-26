package com.example.omega;

import com.example.omega.enums.ContractStatuses;
import com.example.omega.model.Contract;
import com.example.omega.model.Item;
import com.example.omega.repository.ContractRepository;
import com.example.omega.request.CreateContractRequest;
import com.example.omega.request.CreateItemRequest;
import com.example.omega.request.UpdateContractRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testuser")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContractControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ContractRepository repository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUpContracts() {

			Item item = Item.builder()
					.status(ContractStatuses.CREATED.name())
					.name("Item 1")
					.quantity(5)
					.supplier("Supplier 1")
					.deleted(false)
					.id(1)
					.build();


			Contract contract1 = Contract.builder()
					.contractNumber("1/2024")
					.deliveryDate(LocalDate.now().plusDays(5))
					.status(ContractStatuses.CREATED.name())
					.deleted(false)
					.advancePaymentDate(LocalDate.now().minusDays(5))
					.buyer("John Doe")
					.items(List.of(item))
					.build();

			Contract contract2 = Contract.builder()
					.contractNumber("2/2024")
					.deliveryDate(LocalDate.now().plusDays(5))
					.status(ContractStatuses.CREATED.name())
					.deleted(false)
					.advancePaymentDate(LocalDate.now().minusDays(5))
					.buyer("Jane Doe")
					.items(List.of(item))
					.build();

		Contract contract3 = Contract.builder()
				.contractNumber("3/2024")
				.deliveryDate(LocalDate.now().plusDays(5))
				.status(ContractStatuses.CREATED.name())
				.deleted(false)
				.advancePaymentDate(LocalDate.now().minusDays(5))
				.buyer("Franko Kasun")
				.items(List.of(item))
				.build();

		repository.saveAllAndFlush(List.of(contract1, contract2, contract3));
	}

	@Test
	void createContract_ShouldReturnOk() throws Exception {

		CreateContractRequest request = CreateContractRequest.builder()
				.advancePaymentDate(LocalDate.now())
				.deliveryDeadline(LocalDate.now().plusDays(5))
				.buyer("John Doe")
				.items(List.of(CreateItemRequest.builder()
						.supplier("Supplier 1")
						.quantity(4)
						.name("Item 1")
						.build()))
				.build();

		String requestBody = objectMapper.writeValueAsString(request);

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/api/contracts/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isOk());
	}

	@Test
	void fetchAllContracts_ShouldReturnContracts() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/contracts/all")
						.param("buyer", "Jane Doe")
						.param("status", "CREATED"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(greaterThan(0))));
	}

	@Test
	void fetchContract_ShouldReturnContract() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/contracts/fetch")
						.param("buyer", "John Doe")
						.param("contractNumber", "1/2024"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.contractNumber").value("1/2024"))
				.andExpect(jsonPath("$.buyer").value("John Doe"));
	}

	@Test
	void updateContract_ShouldReturnOk() throws Exception {

		UpdateContractRequest request = UpdateContractRequest.builder()
				.advancePaymentDate(LocalDate.now().plusDays(1))
				.deliveryDeadline(LocalDate.now().plusDays(10))
				.buyer("Jane Doe")
				.status(ContractStatuses.ORDERED.name())
				.build();

		String requestBody = objectMapper.writeValueAsString(request);

		mockMvc.perform(MockMvcRequestBuilders.patch("/v1/api/contracts/update")
						.param("contractNumber", "3/2024")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isOk());
	}

	@Test
	void softDeleteContract_ShouldReturnOk() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.delete("/v1/api/contracts/delete")
						.param("contractNumber", "2/2024"))
				.andExpect(status().isOk());
	}

}
