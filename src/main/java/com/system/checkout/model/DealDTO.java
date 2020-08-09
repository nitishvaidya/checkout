package com.system.checkout.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class DealDTO {

	private UUID id;

	private String description;

	private Integer buyQuantity;

	private Map<UUID, Integer> freeProducts;

	private LocalDateTime applicableTill;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getBuyQuantity() {
		return buyQuantity;
	}

	public void setBuyQuantity(Integer buyQuantity) {
		this.buyQuantity = buyQuantity;
	}

	public Map<UUID, Integer> getFreeProducts() {
		return freeProducts;
	}

	public void setFreeProducts(Map<UUID, Integer> freeProducts) {
		this.freeProducts = freeProducts;
	}

	public LocalDateTime getApplicableTill() {
		return applicableTill;
	}

	public void setApplicableTill(LocalDateTime applicableTill) {
		this.applicableTill = applicableTill;
	}

}
