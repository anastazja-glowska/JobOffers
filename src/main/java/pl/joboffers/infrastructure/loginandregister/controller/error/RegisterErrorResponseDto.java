package pl.joboffers.infrastructure.loginandregister.controller.error;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record RegisterErrorResponseDto(String message, HttpStatus status) {
}
