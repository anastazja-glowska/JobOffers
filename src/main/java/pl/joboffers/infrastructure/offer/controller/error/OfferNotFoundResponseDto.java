package pl.joboffers.infrastructure.offer.controller.error;

import org.springframework.http.HttpStatus;

public record OfferNotFoundResponseDto(String message, HttpStatus status) {
}
