package pl.joboffers.infrastructure.loginandregister.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.joboffers.domain.loginandregister.LoginAndRegisterFacade;
import pl.joboffers.domain.loginandregister.dto.UserRegisterRequestDto;
import pl.joboffers.domain.loginandregister.dto.UserRegisterResponseDto;

@RestController
@RequiredArgsConstructor
class RegisterController {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> register(@RequestBody @Valid UserRegisterRequestDto registerUserDto) {

        String encodedPassword = passwordEncoder.encode(registerUserDto.password());
        UserRegisterResponseDto registered = loginAndRegisterFacade.register(
                new UserRegisterRequestDto(registerUserDto.username(), encodedPassword)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(registered);

    }

}
