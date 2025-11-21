package pl.joboffers.infrastructure.token.controller.error;

import org.springframework.http.HttpStatus;

public record TokenErrorResponseDto(String message, HttpStatus status) {
}
