package pl.joboffers.domain.offer;

public class OfferAlreadyExistsException extends RuntimeException {
    public OfferAlreadyExistsException(String message) {
        super(message);
    }
}
