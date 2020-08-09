package com.system.checkout.validator.impl;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.system.checkout.exception.CustomException;
import com.system.checkout.model.DealDTO;
import com.system.checkout.validator.Validator;

@Component
public class DealValidator implements Validator<DealDTO> {

	@Override
	public void validate(DealDTO dealDTO) throws CustomException {
		String errorMessage = "";

		if (StringUtils.isEmpty(dealDTO.getDescription())) {
			errorMessage = errorMessage + "description must not be empty" + ", ";
		}
		if (dealDTO.getBuyQuantity() == null || dealDTO.getBuyQuantity() <= 0) {
			errorMessage = errorMessage + "minimum buy quantity must be greater than zero" + ", ";
		}
		if (dealDTO.getApplicableTill() == null) {
			errorMessage = errorMessage + "deal valid till must be specified" + ", ";
		}
		if (dealDTO.getFreeProducts() == null || dealDTO.getFreeProducts().keySet().size() == 0) {
			errorMessage = errorMessage + "atleast one free product must specified" + ", ";
		}
		if (!StringUtils.isEmpty(errorMessage)) {
			throw new CustomException(errorMessage);
		}
	}

}
