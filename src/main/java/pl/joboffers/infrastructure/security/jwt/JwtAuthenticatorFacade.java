package pl.joboffers.infrastructure.security.jwt;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.joboffers.infrastructure.token.controller.JwtResponseDto;
import pl.joboffers.infrastructure.token.controller.TokenRequestDto;

@RequiredArgsConstructor
@Component
public class JwtAuthenticatorFacade {

    private final AuthenticationManager authenticationManager;


    public JwtResponseDto authenticateAndGenerateToken(TokenRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        return JwtResponseDto.builder().build();
    }
}
