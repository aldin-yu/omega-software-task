package com.example.omega.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "CONTRACTS")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contract {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "buyer", nullable = false)
	private String buyer;

	@Column(name = "contract_number", nullable = true)
	private String contractNumber;

	@Column(name = "advance_payment_date", nullable = false)
	private LocalDate advancePaymentDate;

	@Column(name = "delivery_date", nullable = false)
	private LocalDate deliveryDate;

	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "deleted", nullable = false)
	private boolean deleted = Boolean.FALSE;
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb", name = "items")
	@JsonProperty("items")
	private List<Item> items;

	@PostPersist
	public void generateBrojUgovora() {
		if (this.id != null) {
			this.contractNumber = this.id + "/" + LocalDate.now().getYear();
		}
	}

}

