package pl.joboffers.infrastructure.offer.http;

class NoContentException extends RuntimeException {
    public NoContentException(String s) {
        super(s);
    }
}
