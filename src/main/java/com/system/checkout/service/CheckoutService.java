package com.system.checkout.service;

import java.util.UUID;

import com.system.checkout.exception.CustomException;
import com.system.checkout.model.BasketDTO;
import com.system.checkout.model.ProductCheckoutDTO;

public interface CheckoutService {

	BasketDTO get() throws CustomException;

	void update(UUID productId, ProductCheckoutDTO productCheckoutDTO) throws CustomException;

	void delete(UUID productId) throws CustomException;

	BasketDTO checkout() throws CustomException;

}
