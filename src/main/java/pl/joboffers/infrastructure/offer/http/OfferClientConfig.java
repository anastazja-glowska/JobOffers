package pl.joboffers.infrastructure.offer.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import pl.joboffers.domain.offer.RemoteOfferFetcher;

import java.time.Duration;
import java.util.List;

@Configuration
public class OfferClientConfig {

    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(JobOffersRestTemplateTimeoutConfig restTemplateTimeoutConfig,
                                     RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {


        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(restTemplateTimeoutConfig.connectionTimeout()))
                .setReadTimeout(Duration.ofMillis(restTemplateTimeoutConfig.readTimeout()))
                .build();
    }

    @Bean
    public RemoteOfferFetcher remoteOfferFetcher(RestTemplate restTemplate,
                                                 @Value("${job.offers.http.client.uri}") String uri,
                                                 @Value("${job.offers.http.client.port}") int port) {
        return new JobOffersRestTemplate(restTemplate, uri, port);

    }


}
