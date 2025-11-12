package pl.joboffers.domain.offer;

public class OfferNotFoundException extends RuntimeException{
    public OfferNotFoundException(String s) {
        super(s);
    }
}
