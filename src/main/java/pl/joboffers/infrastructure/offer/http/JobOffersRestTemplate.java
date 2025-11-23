package pl.joboffers.infrastructure.offer.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import pl.joboffers.domain.offer.RemoteOfferFetcher;
import pl.joboffers.domain.offer.dto.RemoteOfferDto;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
public class JobOffersRestTemplate implements RemoteOfferFetcher {

    private static final String OFFERS = "/offers";
    private static final String OFFERS_NOT_FOUND = "Offers on remote server not found";


    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;


    @Override
    public List<RemoteOfferDto> fetchOffersFromServer() throws JsonProcessingException {


        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        final HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);

        try{
            List<RemoteOfferDto> remoteOfferDtos = retrieveResponseBody(entity);

            if(remoteOfferDtos == null || remoteOfferDtos.isEmpty()){
                log.warn(OFFERS_NOT_FOUND);
                return List.of();
            }

            log.info("Retrieved {} offers", remoteOfferDtos.size());
            return remoteOfferDtos;



        } catch (ResourceAccessException e) {
            log.error("Resource access exception was thrown");
            throw new ResourceAccessException("500 INTERNAL_SERVER_ERROR");
        }




    }



    private List<RemoteOfferDto> retrieveResponseBody(HttpEntity<HttpHeaders> entity) throws JsonProcessingException {


        String uriString = UriComponentsBuilder.fromHttpUrl(uri + ":" + port + OFFERS).toUriString();
        ObjectMapper objectMapper = new ObjectMapper();

        ResponseEntity<String> response = restTemplate.exchange(uriString,
                HttpMethod.GET,
                entity,
                String.class);

        validateResponse(response);

        String body = response.getBody();

        if(body == null || body.isEmpty()){
            throw new NoContentException("204 NO_CONTENT");
        }

        List<RemoteOfferDto> offers = objectMapper.readValue(body, new TypeReference<>() {});

        return offers;
    }

    private void validateResponse(ResponseEntity<String> response){
        HttpStatus status = (HttpStatus) response.getStatusCode();
        switch (status){
            case NO_CONTENT -> throw new NoContentException("204 NO_CONTENT");
            case NOT_FOUND -> throw new ResponseStatusException(HttpStatus.NOT_FOUND, "404 NOT_FOUND");
            case UNAUTHORIZED -> throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "401 UNAUTHORIZED");
            default -> { }
        }
    }

}
