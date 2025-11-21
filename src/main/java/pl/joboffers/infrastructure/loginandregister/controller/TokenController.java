package pl.joboffers.infrastructure.loginandregister.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.joboffers.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import pl.joboffers.infrastructure.loginandregister.controller.dto.TokenRequestDto;
import pl.joboffers.infrastructure.security.jwt.JwtAuthenticatorFacade;

@RestController
@RequiredArgsConstructor
class TokenController {

    private final JwtAuthenticatorFacade jwtAuthenticatorFacade;

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> authenticateAndGenerateToken(@RequestBody @Valid TokenRequestDto loginRequest){
        JwtResponseDto jwtResponseDto = jwtAuthenticatorFacade.authenticateAndGenerateToken(loginRequest);
        return ResponseEntity.ok(jwtResponseDto);
    }
}
