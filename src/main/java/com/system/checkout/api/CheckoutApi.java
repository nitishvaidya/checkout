package com.system.checkout.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.system.checkout.exception.CustomException;
import com.system.checkout.model.DealDTO;
import com.system.checkout.model.DiscountDTO;
import com.system.checkout.model.ProductCheckoutDTO;
import com.system.checkout.model.ProductDTO;

public interface CheckoutApi {
	
	@PutMapping("v1/baskets/me/v1/products/{id}")
	@CrossOrigin
	public ResponseEntity<?> update(@PathVariable("id") UUID productId, @RequestBody ProductCheckoutDTO productCheckoutDTO) throws CustomException;
	
	@DeleteMapping("v1/baskets/me/v1/products/{id}")
	@CrossOrigin
	public ResponseEntity<?> delete(@PathVariable("id") UUID productId) throws CustomException;
	
	@PostMapping("v1/baskets/me/checkout")
	@CrossOrigin
	public ResponseEntity<?> checkout() throws CustomException;

	@GetMapping("v1/baskets/me")
	@CrossOrigin
	public ResponseEntity<?> get() throws CustomException;

}
