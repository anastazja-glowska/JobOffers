package pl.joboffers.infrastructure.offer.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

class RestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus httpStatus = HttpStatus.valueOf(response.getRawStatusCode());
        HttpStatus.Series series = httpStatus.series();

        if(series == HttpStatus.Series.SERVER_ERROR){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while using http client");

        } else if (series == HttpStatus.Series.CLIENT_ERROR) {
            if(httpStatus == HttpStatus.NOT_FOUND){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
            } else if (httpStatus == HttpStatus.UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
        }
    }
}
