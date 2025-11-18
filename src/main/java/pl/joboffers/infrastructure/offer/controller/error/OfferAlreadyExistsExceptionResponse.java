package pl.joboffers.infrastructure.offer.controller.error;

import org.springframework.http.HttpStatus;

public record OfferAlreadyExistsExceptionResponse(String message, HttpStatus status) {
}
