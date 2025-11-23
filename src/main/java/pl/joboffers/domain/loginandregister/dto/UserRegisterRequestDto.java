package pl.joboffers.domain.loginandregister.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRegisterRequestDto(
        @NotNull(message = "{email.not.null}")
        @NotEmpty(message = "{email.not.empty}")
        @Email(message = "{email.not.valid}")
        String username,

        @NotNull(message = "{password.not.null}")
        @NotEmpty(message = "{password.not.empty}")
        @Size(min = 6, message = "{password.not.valid}")
        String password) {
}
