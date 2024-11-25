package com.example.omega.controller;



import com.example.omega.model.Item;
import com.example.omega.request.UpdateItemRequest;
import com.example.omega.service.ContractItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/api/items")
public class ItemController {

	private final ContractItemService contractItemService;

	@GetMapping("/fetch")
	public List<Item> fetchItems(@RequestParam String contractNumber) {
		return contractItemService.fetchItems(contractNumber);
	}
	@PatchMapping("/update")
	public void updateItems (@RequestParam String contractNumber, @RequestBody @Valid List<UpdateItemRequest> updateItemRequest) {
		contractItemService.updateContractItems(contractNumber, updateItemRequest);
	}

	@DeleteMapping("/delete")
	public void softDeleteItems(@RequestParam String contractNumber, @RequestBody List<Integer> itemIds) {
		contractItemService.deleteItems(contractNumber, itemIds);
	}
}
