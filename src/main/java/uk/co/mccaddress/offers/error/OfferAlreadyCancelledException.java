package uk.co.mccaddress.offers.error;

import org.springframework.http.HttpStatus;

public class OfferAlreadyCancelledException extends OfferException {
	
	private static final HttpStatus STATUS = HttpStatus.PRECONDITION_FAILED;
	
	public HttpStatus getStatus() {
		return STATUS;
	}
	
}
