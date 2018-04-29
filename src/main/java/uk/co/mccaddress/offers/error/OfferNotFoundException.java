package uk.co.mccaddress.offers.error;

import org.springframework.http.HttpStatus;

public class OfferNotFoundException extends OfferException {
	
	private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
	
	public HttpStatus getStatus() {
		return STATUS;
	}
	
}
