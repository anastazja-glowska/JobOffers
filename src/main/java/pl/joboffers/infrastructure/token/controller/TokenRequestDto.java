package pl.joboffers.infrastructure.token.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TokenRequestDto(
        @NotBlank(message = "{username.not.blank}")
        String username,

        @NotBlank(message = "{password.not.blank}")
        String password) {
}
