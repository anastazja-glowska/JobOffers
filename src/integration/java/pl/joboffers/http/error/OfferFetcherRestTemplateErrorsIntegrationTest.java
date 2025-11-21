package pl.joboffers.http.error;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;
import pl.joboffers.WireMockJobOffersResponse;
import pl.joboffers.domain.offer.RemoteOfferFetcher;
import pl.joboffers.domain.offer.dto.RemoteOfferDto;
import pl.joboffers.infrastructure.offer.http.NoContentException;

import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

class OfferFetcherRestTemplateErrorsIntegrationTest implements WireMockJobOffersResponse {


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


    @Test
    @DisplayName("Should return null offers when external server fault random data then close")
    void should_return_null_offers_when_external_server_fault_random_data_then_close(){
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE_HEADER_KEY, CONTENT_TYPE_VALUE)
                        .withFault(Fault.RANDOM_DATA_THEN_CLOSE)
                ));

        //when

        Throwable throwable = catchThrowable(() -> remoteOfferFetcher.fetchOffersFromServer());

        // then
        assertAll(
                () -> assertThat(throwable).isInstanceOf(ResourceAccessException.class),
                () -> assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR")
        );


    }

    @Test
    @DisplayName("Should throw internal server error when delay is 5000 ms and client has 2000 ms read timeout")
    void should_throw_internal_server_error_when_delay_is_5000_ms_and_client_has_2000_ms_read_timeout(){

        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE_HEADER_KEY, CONTENT_TYPE_VALUE)
                        .withBody(retrieveFourOffersJson())
                        .withFixedDelay(5000)
                ));

        //when

        Throwable throwable = catchThrowable(() -> remoteOfferFetcher.fetchOffersFromServer());

        // then
        assertAll(
                () -> assertThat(throwable).isInstanceOf(ResourceAccessException.class),
                () -> assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR")
        );


    }


    @Test
    @DisplayName("Should throw exception no content when status is 204 no content")
    void should_throw_exception_no_content_when_status_is_204_no_content(){

        //given

        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NO_CONTENT.value())
                        .withHeader(CONTENT_TYPE_HEADER_KEY, CONTENT_TYPE_VALUE)
                        .withBody(retrieveFourOffersJson())));

        //when

        Throwable throwable = catchThrowable(() -> remoteOfferFetcher.fetchOffersFromServer());

        // then
        assertAll(
                () -> assertThat(throwable).isInstanceOf(NoContentException.class),
                () -> assertThat(throwable.getMessage()).isEqualTo("204 NO_CONTENT")
        );


    }


    @Test
    @DisplayName("Should throw not found 404 exception when external server return not found status")
    void should_throw_not_found_404_exception_when_external_server_return_not_found_status(){

        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader(CONTENT_TYPE_HEADER_KEY, CONTENT_TYPE_VALUE)));

        //when

        Throwable throwable = catchThrowable(() -> remoteOfferFetcher.fetchOffersFromServer());

        // then
        assertAll(
                () -> assertThat(throwable).isInstanceOf(ResponseStatusException.class)
        );

    }
}
