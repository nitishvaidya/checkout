package com.system.checkout.validation;

import java.math.BigDecimal;
import java.util.HashMap;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.system.checkout.exception.CustomException;
import com.system.checkout.mocks.MockData;
import com.system.checkout.model.DealDTO;
import com.system.checkout.model.DiscountDTO;
import com.system.checkout.model.ProductDTO;
import com.system.checkout.validator.impl.DealValidator;
import com.system.checkout.validator.impl.DiscountValidator;
import com.system.checkout.validator.impl.ProductValidator;

public class ValidatorTest {

	private ProductValidator productValidator = new ProductValidator();

	private DiscountValidator discountValidator = new DiscountValidator();

	private DealValidator dealValidator = new DealValidator();

	private MockData mockData = new MockData();

	@Test
	public void shouldThrowValidationErrorForProduct() throws CustomException {
		Exception exception = Assertions.assertThrows(CustomException.class, () -> {
			productValidator.validate(new ProductDTO());
		});
		Assertions.assertTrue(exception.getMessage().contains("name must not be empty"));
		Assertions.assertTrue(exception.getMessage().contains("description must not be empty"));
		Assertions.assertTrue(exception.getMessage().contains("quantity must be greater than zero"));
		Assertions.assertTrue(exception.getMessage().contains("price should be positive"));
	}
	
	@Test
	public void shouldThrowValidationErrorForInvalidProduct() throws CustomException {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setPrice(BigDecimal.valueOf(-1000));
		productDTO.setQuantity(-1000);
		Exception exception = Assertions.assertThrows(CustomException.class, () -> {
			productValidator.validate(productDTO);
		});
		Assertions.assertTrue(exception.getMessage().contains("name must not be empty"));
		Assertions.assertTrue(exception.getMessage().contains("description must not be empty"));
		Assertions.assertTrue(exception.getMessage().contains("quantity must be greater than zero"));
		Assertions.assertTrue(exception.getMessage().contains("price should be positive"));
	}


	@Test
	public void shouldThrowValidationErrorForDiscount() throws CustomException {
		Exception exception = Assertions.assertThrows(CustomException.class, () -> {
			discountValidator.validate(new DiscountDTO());
		});
		Assertions.assertTrue(exception.getMessage().contains("discount valid till must be specified"));
		Assertions.assertTrue(exception.getMessage().contains("description must not be empty"));
		Assertions.assertTrue(exception.getMessage().contains("minimum buy quantity must be greater than zero"));
		Assertions.assertTrue(exception.getMessage().contains("discount percentage must be be between 0-100"));
	}
	
	@Test
	public void shouldThrowErrorForInvalidDiscount() throws CustomException {
		DiscountDTO discountDTO = new DiscountDTO();
		discountDTO.setBuyQuantity(-10000);
		discountDTO.setPercentage(10000);
		Exception exception = Assertions.assertThrows(CustomException.class, () -> {
			discountValidator.validate(discountDTO);
		});
		Assertions.assertTrue(exception.getMessage().contains("discount valid till must be specified"));
		Assertions.assertTrue(exception.getMessage().contains("description must not be empty"));
		Assertions.assertTrue(exception.getMessage().contains("minimum buy quantity must be greater than zero"));
		Assertions.assertTrue(exception.getMessage().contains("discount percentage must be be between 0-100"));
	}
	
	@Test
	public void shouldThrowValidationErrorForDeal() throws CustomException {
		Exception exception = Assertions.assertThrows(CustomException.class, () -> {
			dealValidator.validate(new DealDTO());
		});
		Assertions.assertTrue(exception.getMessage().contains("minimum buy quantity must be greater than zero"));
		Assertions.assertTrue(exception.getMessage().contains("description must not be empty"));
		Assertions.assertTrue(exception.getMessage().contains("deal valid till must be specified"));
		Assertions.assertTrue(exception.getMessage().contains("atleast one free product must specified"));

	}
	
	@Test
	public void shouldThrowErrorForInvalidDeal() throws CustomException {
		DealDTO dealDTO = new DealDTO();
		dealDTO.setBuyQuantity(-10000);
		dealDTO.setFreeProducts(new HashMap<>());
		Exception exception = Assertions.assertThrows(CustomException.class, () -> {
			dealValidator.validate(dealDTO);
		});
		Assertions.assertTrue(exception.getMessage().contains("minimum buy quantity must be greater than zero"));
		Assertions.assertTrue(exception.getMessage().contains("description must not be empty"));
		Assertions.assertTrue(exception.getMessage().contains("deal valid till must be specified"));
		Assertions.assertTrue(exception.getMessage().contains("atleast one free product must specified"));

	}
}
