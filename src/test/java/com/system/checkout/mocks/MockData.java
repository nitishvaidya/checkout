package com.system.checkout.mocks;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.system.checkout.entity.BasketEntity;
import com.system.checkout.entity.DealEntity;
import com.system.checkout.entity.DiscountEntity;
import com.system.checkout.entity.ProductEntity;
import com.system.checkout.model.DealDTO;
import com.system.checkout.model.DiscountDTO;
import com.system.checkout.model.ProductCheckoutDTO;
import com.system.checkout.model.ProductDTO;
import com.system.checkout.type.CheckoutStatus;

public class MockData {

	public String PRODUCT_NAME = "product";
	public UUID PRODUCT_ID = UUID.randomUUID();
	public UUID DISCOUNT_ID = UUID.randomUUID();
	public UUID DEAL_ID = UUID.randomUUID();
	public UUID BASKET_ID = UUID.randomUUID();

	public ProductDTO getProdctDTO() {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setName(PRODUCT_NAME);
		productDTO.setDescription("description");
		productDTO.setPrice(BigDecimal.TEN);
		productDTO.setQuantity(10);
		return productDTO;
	}

	public DiscountDTO getDiscountDTO() {
		DiscountDTO discountDTO = new DiscountDTO();
		discountDTO.setDescription("description");
		discountDTO.setBuyQuantity(2);
		discountDTO.setApplicableTill(LocalDateTime.now());
		discountDTO.setPercentage(10);
		return discountDTO;
	}

	public DealDTO getDealDTO() {
		DealDTO dealDTO = new DealDTO();
		dealDTO.setDescription("description");
		dealDTO.setBuyQuantity(2);
		dealDTO.setFreeProducts(getFreeProducts());
		dealDTO.setApplicableTill(LocalDateTime.now());
		return dealDTO;
	}

	public ProductEntity getProductEntity() {
		ProductEntity productEntity = new ProductEntity();
		productEntity.setId(PRODUCT_ID);
		productEntity.setName(PRODUCT_NAME);
		productEntity.setDescription("desc");
		productEntity.setPrice(BigDecimal.TEN);
		productEntity.setQuantity(10);
		productEntity.setLastUpdatedAt(LocalDateTime.now());
		return productEntity;
	}

	public DiscountEntity getDiscountEntity() {
		DiscountEntity discountEntity = new DiscountEntity();
		discountEntity.setId(DISCOUNT_ID);
		discountEntity.setProductId(PRODUCT_ID);
		discountEntity.setDescription("description");
		discountEntity.setBuyQuantity(2);
		discountEntity.setPercentage(10);
		discountEntity.setLastUpdatedAt(LocalDateTime.now());
		return discountEntity;
	}

	public DealEntity getDealEntity() {
		DealEntity dealEntity = new DealEntity();
		dealEntity.setId(DEAL_ID);
		dealEntity.setProductId(PRODUCT_ID);
		dealEntity.setDescription("description");
		dealEntity.setBuyQuantity(2);
		dealEntity.setProducts(getFreeProducts());
		dealEntity.setLastUpdatedAt(LocalDateTime.now());
		return dealEntity;
	}

	public Map getFreeProducts() {
		Map<UUID, Integer> freeProducts = new HashMap<>();
		freeProducts.put(PRODUCT_ID, 1);
		return freeProducts;
	}

	public BasketEntity getBasketEntity() {
		Map<UUID, Integer> products = new HashMap<>();
		products.put(PRODUCT_ID, 5);

		BasketEntity basketEntity = new BasketEntity();
		basketEntity.setId(BASKET_ID);
		basketEntity.setDiscounts(new ArrayList<>());
		basketEntity.setDeals(new ArrayList<>());
		basketEntity.setProducts(products);
		basketEntity.setLastUpdatedAt(LocalDateTime.now());
		basketEntity.setStatus(CheckoutStatus.BASKET);
		return basketEntity;
	}

	public ProductCheckoutDTO productCheckoutDTO() {
		ProductCheckoutDTO productCheckoutDTO = new ProductCheckoutDTO();
		productCheckoutDTO.setQuantity(1);
		return productCheckoutDTO;
	}

}
