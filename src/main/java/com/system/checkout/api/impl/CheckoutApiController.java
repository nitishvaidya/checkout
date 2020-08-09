package com.system.checkout.api.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.system.checkout.api.CheckoutApi;
import com.system.checkout.exception.CustomException;
import com.system.checkout.model.ProductCheckoutDTO;
import com.system.checkout.service.CheckoutService;

@Controller
public class CheckoutApiController implements CheckoutApi {

	@Autowired
	private CheckoutService checkoutService;

	@Override
	public ResponseEntity<?> update(UUID productId, ProductCheckoutDTO productCheckoutDTO) throws CustomException {
		checkoutService.update(productId, productCheckoutDTO);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}

	@Override
	public ResponseEntity<?> delete(UUID productId) throws CustomException {
		checkoutService.delete(productId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}

	@Override
	public ResponseEntity<?> checkout() throws CustomException {
		return ResponseEntity.ok(checkoutService.checkout());
	}

	@Override
	public ResponseEntity<?> get() throws CustomException {
		return ResponseEntity.ok(checkoutService.get());
	}

}
