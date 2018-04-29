package uk.co.mccaddress.offers.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferDTO {
	
	private Long id;
	private String description;
	private String currency;
	private BigDecimal amount;
	private Integer expiryHours;
	private boolean expired;
	private boolean cancelled;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setExpiryHours(Integer expiryHours) {
		this.expiryHours = expiryHours;
	}
	
	public Integer getExpiryHours() {
		return expiryHours;
	}
	
	public boolean isExpired() {
		return expired;
	}
	
	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
