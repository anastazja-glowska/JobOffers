package pl.joboffers.http.error;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.extension.RegisterExtension;
import pl.joboffers.domain.offer.RemoteOfferFetcher;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

class OfferFetcherRestTemplateErrorsIntegrationTest {


    public static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/json";

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    RemoteOfferFetcher offerFetcherConfig =
            new OfferFetcherRestTemplateTestIntegrationConfig()
                    .remoteOfferFetcher(wireMockServer.getPort(), 2000, 2000);


}
