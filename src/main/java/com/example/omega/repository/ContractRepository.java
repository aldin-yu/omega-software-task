package com.example.omega.repository;

import com.example.omega.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

	List<Contract> findAllByBuyerAndStatusAndDeletedFalse (String buyer, String status);

	Optional<Contract> findByContractNumberAndDeletedFalse(String contractNumber);

}
