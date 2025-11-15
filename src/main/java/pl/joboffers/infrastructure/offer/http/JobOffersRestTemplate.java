package pl.joboffers.infrastructure.offer.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.joboffers.domain.offer.RemoteOfferFetcher;
import pl.joboffers.domain.offer.dto.RemoteOfferDto;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
class JobOffersRestTemplate implements RemoteOfferFetcher {

    private static final String BASE_URL = "http://localhost";
    private static final String OFFERS = "/offers";


    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;


    @Override
    public List<RemoteOfferDto> fetchOffersFromServer() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        final HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);


        String remoteOfferDtos = retrieveResponseBody(entity);

        if(remoteOfferDtos == null || remoteOfferDtos.isEmpty()){
            log.warn("No offers found");
            return List.of();
        }

        log.info("Raw remote offer dtos " + remoteOfferDtos);
        List<RemoteOfferDto> offerDtos;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
             offerDtos= objectMapper.readValue(remoteOfferDtos, new TypeReference<List<RemoteOfferDto>>() {
            });
             return offerDtos;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new JsonMappingResponseException("Error while parsing json!");
        }


    }

    private String retrieveResponseBody(HttpEntity<HttpHeaders> entity) {
        String uri = UriComponentsBuilder.fromHttpUrl(BASE_URL + ":" + port + OFFERS).toUriString();

        ResponseEntity<String> response = restTemplate.exchange(uri,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                });

        String remoteOfferDtos = response.getBody();
        return remoteOfferDtos;
    }
}
