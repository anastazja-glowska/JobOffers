package pl.joboffers.infrastructure.offer.controller.error;


import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.joboffers.domain.offer.OfferAlreadyExistsException;
import pl.joboffers.domain.offer.OfferNotFoundException;
import pl.joboffers.infrastructure.offer.controller.OfferRestController;

@ControllerAdvice(basePackageClasses = OfferRestController.class)
@Log4j2
public class OfferControllerExceptionHandler {

    private static final String OFFER_NOT_FOUND = "Offer not found!";
    private static final String OFFER_ALREADY_EXISTS = "Offer already exists!";

    @ExceptionHandler(OfferNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public OfferNotFoundResponseDto handleOfferNotFoundException(OfferNotFoundException exception){
        final String message = exception.getMessage();
        log.error(OFFER_NOT_FOUND + message);
        return new OfferNotFoundResponseDto(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OfferAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public OfferAlreadyExistsExceptionResponseDto handleOfferAlreadyExistsException(OfferAlreadyExistsException e){
        final String message = e.getMessage();
        log.error(OFFER_ALREADY_EXISTS +  message);
        return new OfferAlreadyExistsExceptionResponseDto(message, HttpStatus.BAD_REQUEST);
    }
}
