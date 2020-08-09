package com.system.checkout.validator;

import com.system.checkout.exception.CustomException;

public interface Validator<T> {
	
	void validate(T me) throws CustomException;

}
