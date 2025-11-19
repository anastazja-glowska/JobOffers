package pl.joboffers.infrastructure.offer.controller.error;

import org.springframework.http.HttpStatus;

public record OfferAlreadyExistsExceptionResponseDto(String message, HttpStatus status) {
}
