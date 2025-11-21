package pl.joboffers.infrastructure.offer.http;

public class NoContentException extends RuntimeException {
    public NoContentException(String s) {
        super(s);
    }
}
