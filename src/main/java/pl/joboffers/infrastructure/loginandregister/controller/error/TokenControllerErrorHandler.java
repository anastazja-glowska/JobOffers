package pl.joboffers.infrastructure.loginandregister.controller.error;

import org.springframework.dao.DuplicateKeyException;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Log4j2
public class TokenControllerErrorHandler {

    public static final String BAD_CREDENTIALS = "Bad Credentials";
    private static final String DUPLICATE_KEY = "Duplicate key exception was thrown";
    private static final String USER_ALREADY_EXISTS = "User with this email already exists";

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public TokenErrorResponseDto handleHadCredentialsException(){
        return new TokenErrorResponseDto(BAD_CREDENTIALS, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public RegisterErrorResponseDto handleDuplicateKeyException(DuplicateKeyException exception){
        log.error(DUPLICATE_KEY);
        return new RegisterErrorResponseDto(USER_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }
}
