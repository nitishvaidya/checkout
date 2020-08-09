package com.system.checkout.validator.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.system.checkout.exception.CustomException;
import com.system.checkout.model.DiscountDTO;
import com.system.checkout.validator.Validator;

@Component
public class DiscountValidator implements Validator<DiscountDTO> {

	@Override
	public void validate(DiscountDTO discountDTO) throws CustomException {
		String errorMessage = "";

		if (StringUtils.isEmpty(discountDTO.getDescription())) {
			errorMessage = errorMessage + "description must not be empty" + ", ";
		}
		if (discountDTO.getBuyQuantity() == null || discountDTO.getBuyQuantity() <= 0) {
			errorMessage = errorMessage + "minimum buy quantity must be greater than zero" + ", ";
		}

		if (discountDTO.getPercentage() == null || discountDTO.getPercentage() <= 0 || discountDTO.getPercentage() >= 100) {
			errorMessage = errorMessage + "discount percentage must be be between 0-100" + ", ";
		}
		if (discountDTO.getApplicableTill() == null) {
			errorMessage = errorMessage + "discount valid till must be specified" + ", ";
		}
		if (!StringUtils.isEmpty(errorMessage)) {
			throw new CustomException(errorMessage);
		}
	}

}
