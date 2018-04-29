package uk.co.mccaddress.offers.repository;

import uk.co.mccaddress.offers.model.Offer;

import java.util.List;

public interface OfferRepository {
	Offer save(Offer offer);
	
	Offer get(Long id);
	
	List<Offer> getAll();
}
