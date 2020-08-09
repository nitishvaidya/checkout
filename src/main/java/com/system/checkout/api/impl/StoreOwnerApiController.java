package com.system.checkout.api.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.system.checkout.api.StoreOwnerApi;
import com.system.checkout.model.DealDTO;
import com.system.checkout.model.DiscountDTO;
import com.system.checkout.model.ProductDTO;
import com.system.checkout.service.StoreOwnerService;

@Controller
public class StoreOwnerApiController implements StoreOwnerApi {

	@Autowired
	private StoreOwnerService storeService;

	@Override
	public ResponseEntity<List<ProductDTO>> getAll() {
		return ResponseEntity.ok(storeService.getAll());
	}

	@Override
	public ResponseEntity<UUID> create(ProductDTO productDTO) throws Exception {
		return ResponseEntity.status(HttpStatus.CREATED).body(storeService.create(productDTO));
	}

	@Override
	public ResponseEntity<Void> update(UUID id, ProductDTO productDTO) throws Exception {
		storeService.update(id, productDTO);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}

	@Override
	public ResponseEntity<Void> delete(UUID id) throws Exception {
		storeService.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}

	@Override
	public ResponseEntity<UUID> create(UUID productId, DiscountDTO discountDTO) throws Exception {
		return ResponseEntity.status(HttpStatus.CREATED).body(storeService.create(productId, discountDTO));
	}

	@Override
	public ResponseEntity<UUID> create(UUID productId, DealDTO dealDTO) throws Exception {
		return ResponseEntity.status(HttpStatus.CREATED).body(storeService.create(productId, dealDTO));
	}

	@Override
	public ResponseEntity<List<DiscountDTO>> getAllDiscounts(UUID productId) {
		return ResponseEntity.ok(storeService.getAllDiscounts(productId));
	}

	@Override
	public ResponseEntity<List<DealDTO>> getAllDeals(UUID productId) {
		return ResponseEntity.ok(storeService.getAllDeals(productId));
	}

}
