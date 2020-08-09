package com.system.checkout.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class DiscountDTO {

	private UUID id;

	private String description;

	private Integer buyQuantity;

	private Integer percentage;

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

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public LocalDateTime getApplicableTill() {
		return applicableTill;
	}

	public void setApplicableTill(LocalDateTime applicableTill) {
		this.applicableTill = applicableTill;
	}

}
