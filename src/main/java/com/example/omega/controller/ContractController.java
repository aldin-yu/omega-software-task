package com.example.omega.controller;



import com.example.omega.model.Contract;
import com.example.omega.request.CreateContractRequest;
import com.example.omega.request.UpdateContractRequest;
import com.example.omega.service.ContractItemService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/contracts")
public class ContractController {

private final ContractItemService contractItemService;

	@PostMapping("/create")
	public void createContract(@RequestBody @Valid CreateContractRequest createContractRequest) {
		contractItemService.createContract(createContractRequest);
	}
	@GetMapping("/all")
	public List<Contract> fetchAllContracts(@RequestParam String buyer, @RequestParam String status) {
		return contractItemService.fetchAllContractsByNameAndStatus(buyer, status);
	}
	@GetMapping("/fetch")
	public Contract fetchContract(@RequestParam String contractNumber) {
		return contractItemService.fetchContract(contractNumber);
	}
	@PatchMapping("/update")
	public void updateContract (@RequestParam  String contractNumber, @RequestBody  @Valid UpdateContractRequest updateContractRequest) {
		contractItemService.updateContract(contractNumber, updateContractRequest);
	}

	@DeleteMapping("/delete")
	public void softDeleteContract(@RequestParam String contractNumber) {
		contractItemService.softDeleteContract(contractNumber);
	}



	}

