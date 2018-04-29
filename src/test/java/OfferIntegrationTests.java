import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;
import uk.co.mccaddress.offers.Application;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.mccaddress.offers.dto.OfferDTO;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.http.HttpStatus.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {Application.class})
public class OfferIntegrationTests {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	// Save a new offer (description, currency, amount, expiry period) returns 201 created and body is saved offer with id
	@Test @DirtiesContext
	public void saveOfferReturnsCreatedStatusAndSavedOfferWithId() {
		// When
		OfferDTO req = buildOffer();
		ResponseEntity<OfferDTO> resp = saveOffer(req);
		
		// Then
		assertEquals(CREATED, resp.getStatusCode());
		assertNotNull(resp.getBody());
		assertNotNull(resp.getBody().getId());
		assertEquals(req.getDescription(), resp.getBody().getDescription());
		assertEquals(req.getCurrency(), resp.getBody().getCurrency());
		assertEquals(req.getAmount(), resp.getBody().getAmount());
		assertEquals(req.getExpiryHours(), resp.getBody().getExpiryHours());
		assertFalse(resp.getBody().isExpired());
	}
	
	// Get offer by id that exists returns 200 OK and body is saved offer (description, currency, amount, expiry period)
	@Test @DirtiesContext
	public void getOfferThatExistsByIdReturnsOKStatusAndSavedOfferWithId() {
		// Given
		OfferDTO req = buildOffer();
		ResponseEntity<OfferDTO> saved = saveOffer(req);
		
		// When
		ResponseEntity<OfferDTO> resp = getOfferById(saved.getBody().getId());
		
		// Then
		assertEquals(OK, resp.getStatusCode());
		assertNotNull(resp.getBody());
		assertEquals(saved.getBody().getId().longValue(), resp.getBody().getId().longValue());
		assertEquals(req.getDescription(), resp.getBody().getDescription());
		assertEquals(req.getCurrency(), resp.getBody().getCurrency());
		assertEquals(req.getAmount(), resp.getBody().getAmount());
		assertEquals(req.getExpiryHours(), resp.getBody().getExpiryHours());
		assertFalse(resp.getBody().isExpired());
	}
	
	// Get offer by id that does not exist returns 404 and no body
	@Test @DirtiesContext
	public void getOfferThatDoesNotExistByIdReturnsNotFoundStatusAndNoBody() {
		// When
		ResponseEntity<OfferDTO> resp = getOfferById(Long.MIN_VALUE);
		
		// Then
		assertEquals(NOT_FOUND, resp.getStatusCode());
		assertNull(resp.getBody());
	}
	
	// Get all offers returns list of all saved offers (description, currency, amount, expiry period)
	@Test @DirtiesContext
	public void getAllOffersWhenSomeExistReturnsOKStatusAndListBody() {
		// Given
		for (int i=0; i<50; i++){
			OfferDTO req = buildOffer();
			saveOffer(req);
		}
		
		// When
		ResponseEntity<List<OfferDTO>> resp = getAllOffers();
		
		// Then
		assertEquals(OK, resp.getStatusCode());
		assertNotNull(resp.getBody());
		assertEquals(50, resp.getBody().size());
	}
	
	// Get all offers when none have been saved returns empty list
	@Test @DirtiesContext
	public void getAllOffersWhenNoneExistReturnsOKStatusAndEmptyListBody() {
		// When
		ResponseEntity<List<OfferDTO>> resp = getAllOffers();
		
		// Then
		assertEquals(OK, resp.getStatusCode());
		assertNotNull(resp.getBody());
		assertTrue(resp.getBody().isEmpty());
	}
	
	// Expired offers from get by id are marked as expired
	@Test @DirtiesContext
	public void getExpiredOfferByIdReturnsOKStatusAndMarkedAsExpired() {
		// Given
		OfferDTO req = buildExpiredOffer();
		ResponseEntity<OfferDTO> saved = saveOffer(req);
		
		// When
		ResponseEntity<OfferDTO> resp = getOfferById(saved.getBody().getId());
		
		// Then
		assertEquals(OK, resp.getStatusCode());
		assertNotNull(resp.getBody());
		assertEquals(saved.getBody().getId().longValue(), resp.getBody().getId().longValue());
		assertEquals(req.getDescription(), resp.getBody().getDescription());
		assertEquals(req.getCurrency(), resp.getBody().getCurrency());
		assertEquals(req.getAmount(), resp.getBody().getAmount());
		assertEquals(req.getExpiryHours(), resp.getBody().getExpiryHours());
		assertTrue(resp.getBody().isExpired());
	}
	
	// Expired offers from get all are marked as expired
	@Test @DirtiesContext
	public void getAllOffersReturnsOKStatusAndCorrectlyMarkedAsExpired() {
		// Given
		for (int i=0; i<5; i++) {
			OfferDTO req = buildExpiredOffer();
			saveOffer(req);
		}
		
		for (int i=0; i<5; i++) {
			OfferDTO req = buildOffer();
			saveOffer(req);
		}
		
		// When
		ResponseEntity<List<OfferDTO>> resp = getAllOffers();
		
		// Then
		assertEquals(OK, resp.getStatusCode());
		assertNotNull(resp.getBody());
		assertEquals(10, resp.getBody().size());
		
		for (OfferDTO saved : resp.getBody()) {
			if (saved.getExpiryHours() == 0) {
				assertTrue(saved.isExpired());
			} else {
				assertFalse(saved.isExpired());
			}
		}
	}
	
	// Cancel a non-expired offer returns 200 and no body
	@Test @DirtiesContext
	public void cancelOfferReturnsOKStatusAndEmptyBody() {
		// Given
		OfferDTO req = buildOffer();
		ResponseEntity<OfferDTO> saved = saveOffer(req);
		
		// When
		ResponseEntity<OfferDTO> resp = cancelOffer(saved.getBody().getId());
		
		// Then
		assertEquals(OK, resp.getStatusCode());
		assertNull(resp.getBody());
	}
	
	// Cancel an expired offer returns 412 and no body
	@Test @DirtiesContext
	public void cancelExpiredOfferReturnsStatusPreconFailedAndEmptyBody() {
		// Given
		OfferDTO req = buildExpiredOffer();
		ResponseEntity<OfferDTO> saved = saveOffer(req);
		
		// When
		ResponseEntity<OfferDTO> resp = cancelOffer(saved.getBody().getId());
		
		// Then
		assertEquals(PRECONDITION_FAILED, resp.getStatusCode());
		assertNull(resp.getBody());
	}
	
	// Cancel a non-existing offer returns 404 and no body
	@Test @DirtiesContext
	public void cancelNonExistingOfferReturnsStatusNotFoundAndEmptyBody() {
		// When
		ResponseEntity<OfferDTO> resp = cancelOffer(Long.MIN_VALUE);
		
		// Then
		assertEquals(NOT_FOUND, resp.getStatusCode());
		assertNull(resp.getBody());
	}
	
	// Cancel an already cancelled offer returns 412 and no body
	@Test @DirtiesContext
	public void cancelAlreadyCancelledOfferReturnsPreconFailedStatusAndEmptyBody() {
		// Given
		OfferDTO req = buildOffer();
		ResponseEntity<OfferDTO> saved = saveOffer(req);
		ResponseEntity<OfferDTO> cancelled = cancelOffer(saved.getBody().getId());
		assertEquals(OK, cancelled.getStatusCode());
		assertNull(cancelled.getBody());
		
		// When
		ResponseEntity<OfferDTO> respRetry = cancelOffer(saved.getBody().getId());
		
		// Then
		assertEquals(PRECONDITION_FAILED, respRetry.getStatusCode());
		assertNull(respRetry.getBody());
	}
	
	// Cancelled offers from get by id are marked as cancelled
	@Test @DirtiesContext
	public void cancelledOfferMarkedAsCancelledWhenGotById() {
		// Given
		OfferDTO req = buildOffer();
		ResponseEntity<OfferDTO> saved = saveOffer(req);
		ResponseEntity<OfferDTO> resp = cancelOffer(saved.getBody().getId());
		assertEquals(OK, resp.getStatusCode());
		assertNull(resp.getBody());
		
		// When
		ResponseEntity<OfferDTO> got = getOfferById(saved.getBody().getId());
		
		// Then
		assertEquals(OK, got.getStatusCode());
		assertNotNull(got.getBody());
		assertTrue(got.getBody().isCancelled());
	}
	
	// Cancelled offers from get all are marked as cancelled
	@Test @DirtiesContext
	public void cancelledOfferMarkedAsCancelledWhenAllOffersGot() {
		// Given
		OfferDTO req = buildOffer();
		ResponseEntity<OfferDTO> saved = saveOffer(req);
		ResponseEntity<OfferDTO> resp = cancelOffer(saved.getBody().getId());
		assertEquals(OK, resp.getStatusCode());
		assertNull(resp.getBody());
		
		// When
		ResponseEntity<List<OfferDTO>> gotAll = getAllOffers();
		
		// Then
		assertEquals(OK, gotAll.getStatusCode());
		assertNotNull(gotAll.getBody());
		assertEquals(1, gotAll.getBody().size());
		assertTrue(gotAll.getBody().get(0).isCancelled());
	}
	
	private ResponseEntity<OfferDTO> cancelOffer(Long id) {
		return restTemplate.exchange("/offers/cancel/" + id, HttpMethod.PUT, null, new ParameterizedTypeReference<OfferDTO>(){});
	}
	
	private OfferDTO buildExpiredOffer() {
		OfferDTO req = new OfferDTO();
		req.setDescription("Offer description");
		req.setCurrency("USD");
		req.setAmount(BigDecimal.TEN);
		req.setExpiryHours(0); // Expire immediately
		return req;
	}
	
	// Util methods
	private ResponseEntity<OfferDTO> saveOffer(OfferDTO req) {
		return restTemplate.postForEntity("/offers", req, OfferDTO.class);
	}
	
	private OfferDTO buildOffer() {
		OfferDTO req = new OfferDTO();
		req.setDescription("Offer description");
		req.setCurrency("USD");
		req.setAmount(BigDecimal.TEN);
		req.setExpiryHours(5);
		return req;
	}
	
	private ResponseEntity<List<OfferDTO>> getAllOffers() {
		return restTemplate.exchange("/offers/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<OfferDTO>>(){});
	}
	
	private ResponseEntity<OfferDTO> getOfferById(Long id) {
		return restTemplate.getForEntity("/offers/" + id, OfferDTO.class);
	}
	
}
