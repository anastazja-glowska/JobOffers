package pl.joboffers.infrastructure.offer.controller.error;

import org.springframework.http.HttpStatus;

public record OfferNotFoundResponse(String message, HttpStatus status) {
}
