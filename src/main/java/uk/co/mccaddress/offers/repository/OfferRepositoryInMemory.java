package uk.co.mccaddress.offers.repository;

import org.springframework.stereotype.Repository;
import uk.co.mccaddress.offers.model.Offer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class OfferRepositoryInMemory implements OfferRepository {
	
	private Map<Long, Offer> offers = new ConcurrentHashMap<>();
	private AtomicLong offerId = new AtomicLong(0L);
	
	@Override
	public Offer save(Offer offer) {
		offer.setId(offerId.incrementAndGet());
		offers.put(offer.getId(), offer);
		return offer;
	}
	
	@Override
	public Offer get(Long id) {
		return offers.get(id);
	}
	
	@Override
	public List<Offer> getAll() {
		List<Offer> offerList = new ArrayList<>(offers.values());
		offerList.sort(Comparator.comparing(Offer::getId));
		return offerList;
	}
}
