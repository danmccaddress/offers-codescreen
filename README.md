# Offers Code Screen Project

## Description
    * Solution for Worldpay code screen
    * In memory storage of offers
    * Offer expiry time is in hours and is relative to the time of saving
    * Currency and amount are handled as separate fields
    * Main class: uk.co.mccaddress.offers.Application
    * Run using Maven: mvn spring-boot:run


## Test strategy
    * Prioritised integration testing of service over unit testing of each layer
    * TDD approach followed:
        Identify test cases from requirements doc
        Create barebones 'hello world' application
        Implement test cases and confirm failures
        Implement code until tests pass
        Clean up the code
    * Each test runs independently
    * Cases:
        Save a new offer (description, currency, amount, expiry period) returns 201 created and body is saved offer with id
        Get offer by id that exists returns 200 OK and body is saved offer (description, currency, amount, expiry period)
        Get offer by id that does not exist returns 404 and no body
        Get all offers returns list of all saved offers (description, currency, amount, expiry period)
        Get all offers when none have been saved returns empty list
        Expired offers from get by id endpoint are marked as expired
        Expired offers from get all endpoint are marked as expired
        Cancel a non-expired offer returns 200 and no body
        Cancel an expired offer returns 412 and no body
        Cancel a non-existing offer returns 404 and no body
        Cancel an already cancelled offer returns 412 and no body
        Cancelled offers from get by id endpoint are marked as cancelled
        Cancelled offers from get all endpoint are marked as cancelled


## Schema
    * Offer JSON request:
    {
        "description":"My offer",
        "currency":"USD",
        "amount": 12.50,
        "expiryHours": "1"
    }
    * Offer JSON response:
    {
        "id": 1,
        "description": "My offer",
        "currency": "USD",
        "amount": 12.5,
        "expiryHours": 1,
        "expired": false,
        "cancelled": false
    }
    

## Endpoints
    * /offers
        POST endpoint to save a new offer
        Returns 201 and saved offer with id
    * /offers/{id}
        GET endpoint to retrieve a single offer by id
        Cancelled offers are marked as 'cancelled'
        Expired offers are marked as 'expired'
        Returns 200 and offer if exists
        Returns 404 and empty body if not found
    * /offers/cancel/{id}
        PUT endpoint to cancel a single offer by id - always has empty response body
        Returns 200 if exists, not expired and not already cancelled
        Returns 404 if not found
        Returns 412 if expired or already cancelled
    * /offers/all
        GET endpoint to retrieve all offers
        Returns 200 and list of offers, or empty list if none exist
        Cancelled offers are marked as 'cancelled'
        Expired offers are marked as 'expired'


## Libraries used
    * This is a Maven project
    * Spring Boot - framework for RESTful Microservices
    * Orika - mapping layer between DTO <-> Entity


## Notes
    * Optimised for saving and single retrievals
    * Retrieving all offers involves a sorting (by id) process
