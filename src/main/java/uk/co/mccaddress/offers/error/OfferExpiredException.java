package uk.co.mccaddress.offers.error;

import org.springframework.http.HttpStatus;

public class OfferExpiredException extends OfferException {
	
	private static final HttpStatus STATUS = HttpStatus.PRECONDITION_FAILED;
	
	public HttpStatus getStatus() {
		return STATUS;
	}
	
}
