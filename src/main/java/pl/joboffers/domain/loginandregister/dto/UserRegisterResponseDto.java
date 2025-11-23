package pl.joboffers.domain.loginandregister.dto;

import lombok.Builder;

@Builder
public record UserRegisterResponseDto(String id, String email, boolean isRegistered) {
}
