package com.system.checkout.model;

import java.util.List;

public class DealCheckoutDTO {

	private String name;

	private List<ProductCheckoutDTO> products;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ProductCheckoutDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductCheckoutDTO> products) {
		this.products = products;
	}
	
	

}
