package pl.joboffers.infrastructure.offer.http;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@Log4j2
public class RestTemplateErrorsHandler {


    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public RestTemplateExceptionResponseDto handleResponseStatusException(ResponseStatusException ex) {
        String message = ex.getMessage();
        HttpStatusCode statusCode = ex.getStatusCode();
        log.error("HTTP error: {} - {}", statusCode, message);
        return new RestTemplateExceptionResponseDto(message, statusCode);
    }
}
