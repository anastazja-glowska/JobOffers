package pl.joboffers.domain.register.dto;

import lombok.Builder;

@Builder
public record UserRegisterResponseDto(String id, String email, boolean isRegistered) {
}
