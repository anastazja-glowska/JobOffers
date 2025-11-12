package pl.joboffers.domain.register.dto;


import lombok.Builder;

@Builder
public record UserDto(String id, String email, String password) {
}
