package pl.joboffers.infrastructure.loginandregister.controller.error;

import org.springframework.http.HttpStatus;

public record TokenErrorResponseDto(String message, HttpStatus status) {
}
