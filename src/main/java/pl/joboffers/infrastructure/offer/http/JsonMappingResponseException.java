package pl.joboffers.infrastructure.offer.http;

public class JsonMappingResponseException extends RuntimeException {
    public JsonMappingResponseException(String s) {
        super(s);
    }
}
