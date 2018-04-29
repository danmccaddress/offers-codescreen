package uk.co.mccaddress.offers.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Offer {
	
	private Long id;
	private String description;
	private String currency;
	private BigDecimal amount;
	private Integer expiryHours;
	private LocalDateTime timestamp;
	private boolean cancelled;
	
	public Offer() {
		this.timestamp = LocalDateTime.now();
	}
	
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
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	public boolean isExpired() {
		return expiryHours != null && timestamp.plusHours(expiryHours).isBefore(LocalDateTime.now());
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void cancel() {
		this.cancelled = true;
	}
}
