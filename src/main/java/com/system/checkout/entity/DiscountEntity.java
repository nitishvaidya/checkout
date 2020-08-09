package com.system.checkout.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "discount")
public class DiscountEntity {
	@Id
	private UUID id;

	private UUID productId;

	private String description;

	private Integer buyQuantity;

	private Integer percentage;

	private LocalDateTime lastUpdatedAt;

	private LocalDateTime applicableTill;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
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

	public LocalDateTime getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}

	public LocalDateTime getApplicableTill() {
		return applicableTill;
	}

	public void setApplicableTill(LocalDateTime applicableTill) {
		this.applicableTill = applicableTill;
	}

}
