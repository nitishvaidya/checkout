package com.system.checkout.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.system.checkout.model.DealDTO;
import com.system.checkout.model.DiscountDTO;
import com.system.checkout.model.ProductDTO;

public interface StoreOwnerApi {

	@GetMapping("v1/products")
	@CrossOrigin
	public ResponseEntity<List<ProductDTO>> getAll();

	@PostMapping("v1/products")
	@CrossOrigin
	public ResponseEntity<UUID> create(@RequestBody ProductDTO productDTO) throws Exception;

	@PutMapping("v1/products/{id}")
	@CrossOrigin
	public ResponseEntity<Void> update(@PathVariable("id") UUID id, @RequestBody ProductDTO productDTO)
			throws Exception;

	@DeleteMapping("v1/products/{id}")
	@CrossOrigin
	public ResponseEntity<Void> delete(@PathVariable("id") UUID id) throws Exception;

	@PostMapping("v1/products/{productId}/discounts")
	@CrossOrigin
	public ResponseEntity<UUID> create(@PathVariable("productId") UUID productId, @RequestBody DiscountDTO discountDTO)
			throws Exception;

	@GetMapping("v1/products/{productId}/discounts")
	@CrossOrigin
	public ResponseEntity<List<DiscountDTO>> getAllDiscounts(@PathVariable("productId") UUID productId);

	@PostMapping("v1/products/{productId}/deals")
	@CrossOrigin
	public ResponseEntity<UUID> create(@PathVariable("productId") UUID productId, @RequestBody DealDTO dealDTO)
			throws Exception;

	@GetMapping("v1/products/{productId}/deals")
	@CrossOrigin
	public ResponseEntity<List<DealDTO>> getAllDeals(@PathVariable("productId") UUID productId);

}
