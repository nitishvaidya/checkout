package com.system.checkout.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "deal")
public class DealEntity {
	@Id
	private UUID id;

	private UUID productId;

	private String description;

	private Integer buyQuantity;

	@ElementCollection
	private Map<UUID, Integer> products;

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

	public Map<UUID, Integer> getProducts() {
		return products;
	}

	public void setProducts(Map<UUID, Integer> products) {
		this.products = products;
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
