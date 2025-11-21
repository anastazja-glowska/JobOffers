package pl.joboffers.infrastructure.loginandregister.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TokenControllerErrorHandler {

    public static final String BAD_CREDENTIALS = "Bad Credentials";

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public TokenErrorResponseDto handleHadCredentialsException(){
        return new TokenErrorResponseDto(BAD_CREDENTIALS, HttpStatus.UNAUTHORIZED);
    }
}
