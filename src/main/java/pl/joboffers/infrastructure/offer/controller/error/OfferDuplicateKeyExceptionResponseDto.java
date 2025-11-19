package pl.joboffers.infrastructure.offer.controller.error;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record OfferDuplicateKeyExceptionResponseDto(String message, HttpStatus status) {
}
