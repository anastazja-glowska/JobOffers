package pl.joboffers.feature;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.WireMockJobOffersResponse;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.domain.offer.OfferRepository;
import pl.joboffers.domain.offer.RemoteOfferFetcher;
import pl.joboffers.domain.offer.dto.OfferDto;
import pl.joboffers.domain.offer.dto.RemoteOfferDto;
import pl.joboffers.infrastructure.offer.scheduler.OffersScheduler;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
class UserLoggedInAndRetrievedOffersTest extends BaseIntegrationTest implements WireMockJobOffersResponse {


    @Autowired
    RemoteOfferFetcher remoteOfferFetcher;

    @Autowired
    OfferFacade offerFacade;

    @Autowired
    OfferRepository offerRepository;



//    @Autowired
//    OffersScheduler offersScheduler;

    @Test
    @DisplayName("Should user register and log in and then he can retrieve offers")
    void should_user_register_and_log_in_and_then_he_can_retrieve_offers() throws Exception {


//        step 1: there are no offers in external HTTP server (http://ec2-3-127-218-34.eu-central-1.compute.amazonaws.com:5057/offers)

            //given

        wireMockServer.stubFor(WireMock.get("/offers")
                        .inScenario("Offers scenario")
                        .whenScenarioStateIs(Scenario.STARTED)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(retrieveZeroOffersJson())
                        ).willSetStateTo("Two Offers"));

        wireMockServer.stubFor(WireMock.get("/offers")
                .inScenario("Offers scenario")
                .whenScenarioStateIs("Two Offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(retrieveTwoOffersJson())));

//        wireMockServer.stubFor(WireMock.get("/offers")
//                .willReturn(WireMock.aResponse()
//                        .withStatus(HttpStatus.OK.value())
//                        .withHeader("Content-Type", "application/json")
//                        .withBody(retrieveZeroOffersJson())));


            //when
        List<RemoteOfferDto> remoteOfferDtos = remoteOfferFetcher.fetchOffersFromServer();
        log.info("Remote offers " + remoteOfferDtos);


//        step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database

        //given

//        offersScheduler.scheduleOffers();

        int initialRepositorySize = offerRepository.findAll().size();
        log.info("Initial repository size " + initialRepositorySize);
        //when

        List<OfferDto> offerDtos = offerFacade.fetchAllOffersAndSaveIfNotExists();
       log.info("Offers " + offerDtos);

        await()
                    .atMost(Duration.ofSeconds(10))
                    .pollInterval(Duration.ofSeconds(1))
                    .until(() -> offerRepository.findAll().size() > initialRepositorySize);

        log.info("Size after : "  +  offerRepository.findAll().size());

//        step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
//        step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
//        step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
//        step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
//        step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers


        //given && when

        MvcResult result = mockMvc.perform(get("/offers").contentType(MediaType.APPLICATION_JSON)).andReturn();
        String json = result.getResponse().getContentAsString();
        List<OfferDto> mappedOffers = objectMapper.readValue(json, new TypeReference<List<OfferDto>>() {
        });

        //then
        log.info("mappedOffers Size " + mappedOffers.size());
        OfferDto expectedIncludedOffer = OfferDto.builder()
                .title("Java Developer")
                .salary("7000 - 9000")
                .company("Tech Solutions")
                .offerUrl("https://techsolutions.com/jobs/java-developer")
                .build();

        assertAll(
                () -> assertThat(mappedOffers).isNotNull(),
                () -> assertThat(mappedOffers.size()).isEqualTo(4),
                () -> assertThat(mappedOffers).extracting(OfferDto::company, OfferDto::salary, OfferDto::title, OfferDto::offerUrl)
                        .contains(tuple(expectedIncludedOffer.company(), expectedIncludedOffer.salary(), expectedIncludedOffer.title(), expectedIncludedOffer.offerUrl()))




        );

//        step 8: there are 2 new offers in external HTTP server
//        step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database
//        step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000
//        step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
        
        //given && when && then

        ResultActions offerAction = mockMvc.perform(get("/offers/9999").contentType(MediaType.APPLICATION_JSON));
        offerAction.andExpect(status().isNotFound()).andExpect(content().json(
                """
                               {
                               "message" : "Offer not found for id 9999",
                               "status" : "NOT_FOUND"
                               }
                               """.trim()
        ));


//        step 12: user made GET /offers/1000 and system returned OK(200) with offer
//        step 13: there are 2 new offers in external HTTP server
//        step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
//        step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000

        //        step 16: user made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer and system returned CREATED(201) with saved offer

        //given && when
        ResultActions resultActions = mockMvc.perform(post("/offers")
                .content(
                        """
                                {
                                  "title": "new offer",
                                  "company": "new company",
                                  "salary": "7000 - 10000",
                                  "offerUrl": "https://new.com"
                                }
                                """
                )
                .contentType(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = resultActions.andExpect(status().isCreated()).andReturn();
        String createdOfferJson = mvcResult.getResponse().getContentAsString();
        OfferDto offerDto = objectMapper.readValue(createdOfferJson, OfferDto.class);
        String offerId = offerDto.id();

        //then
        assertThat(offerDto).isNotNull();
        assertThat(offerDto.offerUrl()).isEqualTo("https://new.com");
        assertThat(offerDto.salary()).isEqualTo("7000 - 10000");
        assertThat(offerDto.company()).isEqualTo("new company");


//        step 17: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 1 offer




    }
}
