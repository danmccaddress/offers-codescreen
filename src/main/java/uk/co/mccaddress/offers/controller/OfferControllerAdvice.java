package uk.co.mccaddress.offers.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.co.mccaddress.offers.error.OfferException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackageClasses = OfferController.class)
public class OfferControllerAdvice extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(OfferException.class)
	@ResponseBody
	ResponseEntity<?> handleControllerException(HttpServletRequest request, OfferException ex) {
		return ResponseEntity.status(ex.getStatus()).build();
	}
}
