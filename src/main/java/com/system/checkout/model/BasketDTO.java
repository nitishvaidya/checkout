package com.system.checkout.model;

import java.math.BigDecimal;
import java.util.List;

public class BasketDTO {

	private List<ProductCheckoutDTO> products;

	private List<String> discounts;

	private List<DealCheckoutDTO> deals;

	private BigDecimal totalPrice;

	public List<ProductCheckoutDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductCheckoutDTO> products) {
		this.products = products;
	}

	public List<String> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(List<String> discounts) {
		this.discounts = discounts;
	}

	public List<DealCheckoutDTO> getDeals() {
		return deals;
	}

	public void setDeals(List<DealCheckoutDTO> deals) {
		this.deals = deals;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	

}
