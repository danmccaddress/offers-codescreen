package uk.co.mccaddress.offers.service;

import uk.co.mccaddress.offers.dto.OfferDTO;

import java.util.List;

public interface OfferService {
	OfferDTO save(OfferDTO offer);
	
	OfferDTO get(Long id);
	
	List<OfferDTO> getAll();
	
	void cancel(Long id);
}
