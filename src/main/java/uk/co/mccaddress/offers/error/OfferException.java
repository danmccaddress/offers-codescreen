package uk.co.mccaddress.offers.error;

import org.springframework.http.HttpStatus;

public abstract class OfferException extends RuntimeException {
	
	public abstract HttpStatus getStatus();
}
