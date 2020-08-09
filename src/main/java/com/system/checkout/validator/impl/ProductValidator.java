package com.system.checkout.validator.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.system.checkout.exception.CustomException;
import com.system.checkout.model.ProductDTO;
import com.system.checkout.validator.Validator;

@Component
public class ProductValidator implements Validator<ProductDTO> {

	@Override
	public void validate(ProductDTO productDTO) throws CustomException {
		String errorMessage = "";
		if (StringUtils.isEmpty(productDTO.getName())) {
			errorMessage = errorMessage + "name must not be empty" + ", ";
		}
		if (StringUtils.isEmpty(productDTO.getDescription())) {
			errorMessage = errorMessage + "description must not be empty" + ", ";
		}
		if (productDTO.getQuantity()== null ||  productDTO.getQuantity() <= 0) {
			errorMessage = errorMessage + "quantity must be greater than zero" + ", ";
		}
		if (productDTO.getPrice() == null || productDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
			errorMessage = errorMessage + "price should be positive" + ", ";
		}

		if (!StringUtils.isEmpty(errorMessage)) {
			throw new CustomException(errorMessage);
		}

	}

}
