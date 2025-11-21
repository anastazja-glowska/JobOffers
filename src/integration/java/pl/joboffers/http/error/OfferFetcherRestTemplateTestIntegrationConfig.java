package pl.joboffers.http.error;

import org.springframework.web.client.RestTemplate;
import pl.joboffers.domain.offer.RemoteOfferFetcher;
import pl.joboffers.infrastructure.offer.http.JobOffersRestTemplateTimeoutConfig;
import pl.joboffers.infrastructure.offer.http.OfferClientConfig;

class OfferFetcherRestTemplateTestIntegrationConfig extends OfferClientConfig {


    public RemoteOfferFetcher remoteOfferFetcher(int port, long connectionTimeout, long readTimeout){

        JobOffersRestTemplateTimeoutConfig jobOffersConfig =
                new JobOffersRestTemplateTimeoutConfig(readTimeout, connectionTimeout);

        RestTemplate restTemplate = restTemplate(jobOffersConfig, restTemplateResponseErrorHandler());

        return remoteOfferFetcher(restTemplate, "http://localhost", port);


    }
}
