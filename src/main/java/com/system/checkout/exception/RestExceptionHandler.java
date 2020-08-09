package com.system.checkout.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
  
   //other exception handlers
  
   @ExceptionHandler(CustomException.class)
   protected ResponseEntity<Object> handle(
		   CustomException ex) {
	   Map<String,Object> errorObject = new HashMap<>();
	   errorObject.put("timestamp", LocalDateTime.now());
	   errorObject.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
	   errorObject.put("errorMessage", ex.getMessage());
       return ResponseEntity.badRequest().body(errorObject);
   }
}
