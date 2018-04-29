package uk.co.mccaddress.offers.service;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.mccaddress.offers.dto.OfferDTO;
import uk.co.mccaddress.offers.error.OfferAlreadyCancelledException;
import uk.co.mccaddress.offers.error.OfferExpiredException;
import uk.co.mccaddress.offers.error.OfferNotFoundException;
import uk.co.mccaddress.offers.model.Offer;
import uk.co.mccaddress.offers.repository.OfferRepository;

import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {
	
	@Autowired
	private OfferRepository offersRepository;
	
	private MapperFacade mapper;
	
	public OfferServiceImpl() {
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		mapper = mapperFactory.getMapperFacade();
	}
	
	@Override
	public OfferDTO save(OfferDTO offer) {
		Offer unsaved = mapper.map(offer, Offer.class);
		
		Offer saved = offersRepository.save(unsaved);
		
		return mapper.map(saved, OfferDTO.class);
	}
	
	@Override
	public OfferDTO get(Long id) {
		Offer offer = offersRepository.get(id);
		
		if (offer == null) {
			throw new OfferNotFoundException();
		}
		
		return mapper.map(offer, OfferDTO.class);
	}
	
	@Override
	public List<OfferDTO> getAll() {
		List<Offer> offers = offersRepository.getAll();
		
		return mapper.mapAsList(offers, OfferDTO.class);
	}
	
	@Override
	public void cancel(Long id) {
		Offer offer = offersRepository.get(id);
		
		if (offer == null) {
			throw new OfferNotFoundException();
		}
		
		if (offer.isExpired()) {
			throw new OfferExpiredException();
		}
		
		if (offer.isCancelled()) {
			throw new OfferAlreadyCancelledException();
		}
		
		offer.cancel();
	}
}
