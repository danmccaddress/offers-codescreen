package uk.co.mccaddress.offers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.mccaddress.offers.dto.OfferDTO;
import uk.co.mccaddress.offers.service.OfferService;

import java.util.List;

@RestController
@RequestMapping(path = "/offers")
public class OfferController {
	
	@Autowired
	private OfferService offersService;
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<OfferDTO> save(@RequestBody OfferDTO offer) {
		OfferDTO saved = offersService.save(offer);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public @ResponseBody ResponseEntity<OfferDTO> get(@PathVariable Long id) {
		OfferDTO offer = offersService.get(id);
		return ResponseEntity.status(HttpStatus.OK).body(offer);
	}
	
	@RequestMapping(method = RequestMethod.PUT, path = "/cancel/{id}")
	public @ResponseBody ResponseEntity<OfferDTO> cancel(@PathVariable Long id) {
		offersService.cancel(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/all")
	public @ResponseBody ResponseEntity<List<OfferDTO>> get() {
		List<OfferDTO> offers = offersService.getAll();
		return ResponseEntity.status(HttpStatus.OK).body(offers);
	}
}
