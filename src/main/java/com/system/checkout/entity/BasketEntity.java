package com.system.checkout.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.system.checkout.model.BasketDTO;
import com.system.checkout.type.CheckoutStatus;
import com.system.checkout.util.CustomAttributeConverter;

@Entity(name = "basket")
public class BasketEntity {
	@Id
	private UUID id;

	@ElementCollection
	private Map<UUID, Integer> products;

	@ElementCollection
	private List<UUID> discounts;

	@ElementCollection
	private List<UUID> deals;
	
	@ElementCollection
	private Map<UUID, Integer> freeProducts;

	private BigDecimal totalPrice;

	private LocalDateTime lastUpdatedAt;

	@Enumerated(EnumType.STRING)
	private CheckoutStatus status;

	@Lob
	@Convert(converter = CustomAttributeConverter.class)
	private BasketDTO summary;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Map<UUID, Integer> getProducts() {
		return products;
	}

	public void setProducts(Map<UUID, Integer> products) {
		this.products = products;
	}

	public List<UUID> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(List<UUID> discounts) {
		this.discounts = discounts;
	}

	public List<UUID> getDeals() {
		return deals;
	}

	public void setDeals(List<UUID> deals) {
		this.deals = deals;
	}

	public Map<UUID, Integer> getFreeProducts() {
		return freeProducts;
	}

	public void setFreeProducts(Map<UUID, Integer> freeProducts) {
		this.freeProducts = freeProducts;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public LocalDateTime getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}

	public CheckoutStatus getStatus() {
		return status;
	}

	public void setStatus(CheckoutStatus status) {
		this.status = status;
	}

	public BasketDTO getSummary() {
		return summary;
	}

	public void setSummary(BasketDTO summary) {
		this.summary = summary;
	}
	
	

}
