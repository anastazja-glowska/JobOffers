package pl.joboffers.infrastructure.offer.http;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Builder
public record RestTemplateExceptionResponseDto(String message, HttpStatusCode status) {
}
