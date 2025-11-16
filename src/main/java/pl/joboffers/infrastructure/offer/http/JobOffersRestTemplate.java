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


    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;


    @Override
    public List<RemoteOfferDto> fetchOffersFromServer() {
        final HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        final HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);


        List<RemoteOfferDto> remoteOfferDtos = retrieveResponseBody(entity);

        if(remoteOfferDtos == null || remoteOfferDtos.isEmpty()){
            log.warn("No offers found");
            return List.of();
        }

        log.info("Raw remote offer dtos " + remoteOfferDtos);
        return remoteOfferDtos;


    }

    private List<RemoteOfferDto> retrieveResponseBody(HttpEntity<HttpHeaders> entity) {
        String uriString = UriComponentsBuilder.fromHttpUrl(uri + ":" + port + OFFERS).toUriString();

        ResponseEntity<List<RemoteOfferDto>> response = restTemplate.exchange(uriString,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                });

        List<RemoteOfferDto> remoteOfferDtos = response.getBody();
        return remoteOfferDtos;
    }


//    private static final String OFFERS = "/offers";
//
//
//    private final RestTemplate restTemplate;
//    private final String uri;
//    private final int port;
//
//
//    @Override
//    public List<RemoteOfferDto> fetchOffersFromServer() {
//
//
//        log.info("Started getting offers using http client");
//        HttpHeaders headers = new HttpHeaders();
////        headers.setContentType(MediaType.APPLICATION_JSON);
////        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
//
//        HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
//        try {
//            String urlForService = getUrlForService(OFFERS);
//
//            final String url = UriComponentsBuilder.fromHttpUrl(urlForService).toUriString();
//            ResponseEntity<List<RemoteOfferDto>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
//                    new ParameterizedTypeReference<>() {
//                    });
//
//
//            log.info("Response headers: {}", response.getHeaders());
//            log.info("Response body: {}", response.getBody());
//
//            final List<RemoteOfferDto> body = response.getBody();
//            if (body == null) {
//                log.error("Response Body was null");
//                return List.of();
//            }
//
//            return body;
//        } catch (ResourceAccessException e) {
//            log.error("Error while fetching offers using http client: " + e.getMessage());
//            return List.of();
//        }
//
//
//    }
//    private String getUrlForService(String service) {
//        return uri + ":" + port + service;
//    }
}
