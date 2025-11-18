package pl.joboffers.infrastructure.offer.controller.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.joboffers.domain.offer.OfferAlreadyExistsException;
import pl.joboffers.domain.offer.OfferNotFoundException;

@ControllerAdvice
@Log4j2
public class OfferControllerExceptionHandler {

    @ExceptionHandler(OfferNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public OfferNotFoundResponse handleOfferNotFoundException(OfferNotFoundException exception){
        String message = exception.getMessage();
        log.error(message);
        return new OfferNotFoundResponse(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OfferAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public OfferAlreadyExistsExceptionResponse handleOfferAlreadyExistsException(OfferAlreadyExistsException e){
        String message = e.getMessage();
        log.error(message);
        return new OfferAlreadyExistsExceptionResponse(message, HttpStatus.BAD_REQUEST);
    }
}
