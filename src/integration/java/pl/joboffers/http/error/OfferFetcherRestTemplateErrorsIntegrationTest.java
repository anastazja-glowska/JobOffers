package pl.joboffers.http.error;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.ResourceAccessException;
import pl.joboffers.domain.offer.RemoteOfferFetcher;
import pl.joboffers.domain.offer.dto.RemoteOfferDto;

import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

class OfferFetcherRestTemplateErrorsIntegrationTest {


    public static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/json";

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    RemoteOfferFetcher remoteOfferFetcher =
            new OfferFetcherRestTemplateTestIntegrationConfig()
                    .remoteOfferFetcher(wireMockServer.getPort(), 2000, 2000);


    @Test
    @DisplayName("Should return_null jobs when external server was reset by peer")
    void should_return_null_jobs_when_external_server_was_reset_by_peer(){
        //given

        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE_HEADER_KEY, CONTENT_TYPE_VALUE)
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)));

        // when
        Throwable throwable = catchThrowable(() -> remoteOfferFetcher.fetchOffersFromServer());

        // then

        assertAll(
                ()  -> assertThat(throwable).isInstanceOf(ResourceAccessException.class),
                () -> assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR")
        );



    }

    @Test
    @DisplayName("Should return null jobs when external server fault empty response")
    void should_return_null_jobs_when_external_server_fault_empty_response(){

        // given

        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value()
                        ).withHeader(CONTENT_TYPE_HEADER_KEY, CONTENT_TYPE_VALUE)
                        .withFault(Fault.EMPTY_RESPONSE)));

        //when
        Throwable throwable = catchThrowable(() -> remoteOfferFetcher.fetchOffersFromServer());

        //then
        assertAll(
                () -> assertThat(throwable).isInstanceOf(ResourceAccessException.class),
                () -> assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR")
        );
    }


    @Test
    @DisplayName("Should return null jobs when external server malformed response chunk")
    void should_return_null_jobs_when_external_server_malformed_response_chunk(){
        //given

        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE_HEADER_KEY, CONTENT_TYPE_VALUE)
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

        //when

        Throwable throwable = catchThrowable(() -> remoteOfferFetcher.fetchOffersFromServer());

        // then
        assertAll(
                () -> assertThat(throwable).isInstanceOf(ResourceAccessException.class),
                () -> assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR")
        );
    }
}
