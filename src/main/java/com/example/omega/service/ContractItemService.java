package com.example.omega.service;


import com.example.omega.enums.ContractStatuses;
import com.example.omega.model.Contract;
import com.example.omega.model.Item;
import com.example.omega.repository.ContractRepository;
import com.example.omega.request.CreateContractRequest;
import com.example.omega.request.UpdateContractRequest;
import com.example.omega.request.UpdateItemRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
@Slf4j
public class ContractItemService {

    private final ContractRepository contractRepository;

    @Transactional
    public void createContract (CreateContractRequest createContractRequest) {

        List<Item> items = new ArrayList<>();
        IntStream.range(0, createContractRequest.getItems().size())
                .mapToObj(i -> Item.builder()
                        .id(i)
                        .name(createContractRequest.getItems().get(i).getName())
                        .quantity(createContractRequest.getItems().get(i).getQuantity())
                        .supplier(createContractRequest.getItems().get(i).getSupplier())
                        .status(ContractStatuses.CREATED.name())
                        .deleted(false).build())
                .forEach(items::add);

        Contract contract = Contract.builder()
                .advancePaymentDate(createContractRequest.getAdvancePaymentDate())
                .buyer(createContractRequest.getBuyer())
                .deliveryDate(createContractRequest.getDeliveryDeadline())
                .status(ContractStatuses.CREATED.name())
                .items(items)
                .deleted(false).build();

        contractRepository.saveAndFlush(contract);

    }

    public List<Contract> fetchAllContractsByNameAndStatus (String buyer, String status) {
        return contractRepository.findAllByBuyerAndStatusAndDeletedFalse(buyer, status);
    }

    public Contract fetchContract (String contractNumber) {

        return contractRepository.findByContractNumberAndDeletedFalse(contractNumber)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found for number: " + contractNumber));
    }

    private boolean isActive (Contract contract) {
        return Set.of(ContractStatuses.CREATED.name(), ContractStatuses.ORDERED.name())
                .contains(contract.getStatus().toUpperCase());
    }

    private void canAlterStatus (String currentStatus, String newStatus) {

        if (currentStatus.equalsIgnoreCase(ContractStatuses.CREATED.name()) && !newStatus.equalsIgnoreCase(ContractStatuses.ORDERED.name())) {
            log.debug("Not possible to change status {} to {}", currentStatus, newStatus);

            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Contracts in status CREATED can only be changed to ORDERED");

        }

        if (currentStatus.equalsIgnoreCase(ContractStatuses.ORDERED.name()) && !newStatus.equalsIgnoreCase(ContractStatuses.DELIVERED.name())) {
            log.debug("Not possible to change status {} to {}", currentStatus, newStatus);
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Contracts in status ORDERED can only be changed to DELIVERED");

        }

    }

    private boolean canBeEdited (Contract contract) {
        if (isActive(contract)) {
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Contract is inactive, cannot change details of the contract");
        }
    }

    public void updateContract (String contractNumber, UpdateContractRequest updateContractRequest) {
        Contract contractToBeUpdated = fetchContract(contractNumber);
        if (canBeEdited(contractToBeUpdated)) {
            log.debug("Updating contract {}", contractNumber);

            String newStatus = updateContractRequest.getStatus();

            canAlterStatus(contractToBeUpdated.getStatus(), newStatus);

            contractToBeUpdated.setStatus(newStatus);
            contractToBeUpdated.setBuyer(updateContractRequest.getBuyer());
            contractToBeUpdated.setDeliveryDate(updateContractRequest.getDeliveryDeadline());
            contractToBeUpdated.setAdvancePaymentDate(updateContractRequest.getAdvancePaymentDate());
            contractRepository.saveAndFlush(contractToBeUpdated);
        }
    }

    public void softDeleteContract (String contractNumber) {
        Contract contract = fetchContract(contractNumber);

        if (!ContractStatuses.CREATED.name().equalsIgnoreCase(contract.getStatus())) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Only contracts in status CREATED can be deleted");
        }

        contract.setDeleted(true);
        contractRepository.saveAndFlush(contract);
    }

    public List<Item> fetchItems (String contractNumber) {
        return contractRepository.findByContractNumberAndDeletedFalse(contractNumber)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found for number: " + contractNumber)).getItems();

    }

    public void deleteItems (String contractNumber, List<Integer> itemIds) {
        Contract contract = fetchContract(contractNumber);

        if (canBeEdited(contract)) {
            validateItemsExist(contract, itemIds);
            contract
                    .getItems()
                    .stream()
                    .distinct()
                    .filter(i -> itemIds.contains(i.getId()))
                    .collect(Collectors.toList())
                    .forEach(item -> item.setDeleted(true));

            contractRepository.saveAndFlush(contract);

        }

    }

    private void validateItemsExist (Contract contract, List<Integer> requestItemIds) {
        List<Integer> itemIds = contract.getItems().stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        requestItemIds.forEach(requestItemId -> {
            if (!itemIds.contains(requestItemId)) {
                throw new ResponseStatusException(
                        HttpStatus.PRECONDITION_FAILED,
                        "Item with ID " + requestItemId + " does not exist in the contract."
                );
            }
        });
    }

    public void updateContractItems (String contractNumber, List<UpdateItemRequest> updateItemRequests) {
        Contract contract = fetchContract(contractNumber);
        if (canBeEdited(contract)) {
            validateItemsExist(contract, updateItemRequests.stream().map(UpdateItemRequest::getId).collect(Collectors.toList()));

            contract.setItems(contract.getItems().stream()
                    .map(item -> {
                        return updateItemRequests.stream()
                                .filter(updateItemRequest -> updateItemRequest.getId() == item.getId())
                                .findFirst()
                                .map(updateItemRequest -> {
                                    item.setQuantity(updateItemRequest.getQuantity());
                                    item.setSupplier(updateItemRequest.getSupplier());
                                    item.setStatus(updateItemRequest.getStatus());
                                    item.setName(updateItemRequest.getName());
                                    return item;
                                })
                                .orElse(item);
                    })
                    .collect(Collectors.toList()));

            contractRepository.saveAndFlush(contract);
        }

    }
}
