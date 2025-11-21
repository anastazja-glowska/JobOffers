package pl.joboffers.feature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
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
class UserLoggedInAndRetrievedOffersIntegrationTest extends BaseIntegrationTest implements WireMockJobOffersResponse {


    @Autowired
    OfferFacade offerFacade;

    @Autowired
    OfferRepository offerRepository;


    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    public static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }


    @Test
    @DisplayName("Should user register and log in and then he can retrieve offers")
    void should_user_register_and_log_in_and_then_he_can_retrieve_offers() throws Exception {


//        step 1: there are no offers in external HTTP server (http://ec2-3-127-218-34.eu-central-1.compute.amazonaws.com:5057/offers)

            //given && when && then

        wireMockServer.stubFor(WireMock.get("/offers")
                        .inScenario("Offers scenario")
                        .whenScenarioStateIs(Scenario.STARTED)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(retrieveZeroOffersJson())
                        ).willSetStateTo("Two Offers"));



//        step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database

        //given && when
        List<OfferDto> initialOfferDtoList = offerFacade.fetchAllOffersAndSaveIfNotExists();

        //then
        assertAll(
                () -> assertThat(initialOfferDtoList).isEmpty(),
                () -> assertThat(initialOfferDtoList).hasSize(0)
        );



//        step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)


        ResultActions failedUserRegister = mockMvc.perform(post("/token")
                .content(
                        retrieveSomeUserWithSomePassword()
                ).contentType(MediaType.APPLICATION_JSON));

        failedUserRegister.andExpect(status().isUnauthorized())
                .andExpect(content().json(
                        """
                                {
                                  "message" : "Bad Credentials",
                                  "status" : "UNAUTHORIZED"
                                }
                                """.trim()
                ));
//        step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
//        step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
//        step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
//        step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers


        //given && when

        MvcResult result = mockMvc.perform(get("/offers").contentType(MediaType.APPLICATION_JSON)).andReturn();
        String jsonWithZeroOffer = result.getResponse().getContentAsString();
        List<OfferDto> mappedZeroOffers = objectMapper.readValue(jsonWithZeroOffer, new TypeReference<List<OfferDto>>() {
        });

        //then

        assertAll(
                () -> assertThat(mappedZeroOffers).isEmpty(),
                () -> assertThat(mappedZeroOffers.size()).isEqualTo(0)

        );

//        step 8: there are 2 new offers in external HTTP server

        // given && when && then

        wireMockServer.stubFor(WireMock.get("/offers")
                .inScenario("Offers scenario")
                .whenScenarioStateIs("Two Offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(retrieveTwoOffersJson())
                ).willSetStateTo("Four Offers"));


        //        step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database

        //given && when

        List<OfferDto> twoOfferDtosList = offerFacade.fetchAllOffersAndSaveIfNotExists();
        int sizeWithExpectedTwoOffers = twoOfferDtosList.size();


        await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> offerRepository.findAll().size() > initialOfferDtoList.size());

        log.info("Size after fetching : "  +  offerRepository.findAll().size());

        // then
        assertThat(twoOfferDtosList).hasSize(2);


//        step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000

        // given && when

        MvcResult resultsWithTwoOffer = mockMvc.perform(get("/offers")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        String json2 = resultsWithTwoOffer.getResponse().getContentAsString();
        List<OfferDto> mappedTwoOffers = objectMapper.readValue(json2, new TypeReference<List<OfferDto>>() {
        });

        //then
        log.info("mappedZeroOffers Size " + mappedTwoOffers.size());
        OfferDto expectedIncludedOffer2 = OfferDto.builder()
                .title("Java Developer")
                .salary("7000 - 9000")
                .company("Tech Solutions")
                .offerUrl("https://techsolutions.com/jobs/java-developer")
                .build();

        assertAll(
                () -> assertThat(mappedTwoOffers).isNotNull(),
                () -> assertThat(mappedTwoOffers.size()).isEqualTo(2),
                () -> assertThat(mappedTwoOffers).extracting(OfferDto::company, OfferDto::salary, OfferDto::title, OfferDto::offerUrl)
                        .contains(tuple(expectedIncludedOffer2.company(), expectedIncludedOffer2.salary(),
                                expectedIncludedOffer2.title(), expectedIncludedOffer2.offerUrl()))




        );

//        step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message Offer with offerUrl [9999] does not exists!
        
        //given && when && then

        ResultActions offerAction = mockMvc.perform(get("/offers/9999").contentType(MediaType.APPLICATION_JSON));
        offerAction.andExpect(status().isNotFound()).andExpect(content().json(
                """
                               {
                               "message" : "Offer with offerUrl [9999] does not exists!",
                               "status" : "NOT_FOUND"
                               }
                               """.trim()
        ));


//        step 12:  user made GET /offers/1000 with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with offer

        // given
        OfferDto savedOfferDto = mappedTwoOffers.get(0);
        String savedOfferId = savedOfferDto.id();

        //when
        MvcResult resultWithRetrievedOffer = mockMvc.perform(get("/offers/" + savedOfferId)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String retrievedOfferJson = resultWithRetrievedOffer.getResponse().getContentAsString();

        OfferDto retrievedOffer = objectMapper.readValue(retrievedOfferJson, OfferDto.class);

        // then

        assertAll(
                () -> assertThat(retrievedOffer).isNotNull(),
                () -> assertThat(resultWithRetrievedOffer.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(retrievedOffer.id()).isEqualTo(savedOfferId),
                () -> assertThat(retrievedOffer.title()).isEqualTo("Java Developer"),
                () -> assertThat(retrievedOffer.salary()).isEqualTo("7000 - 9000"),
                () -> assertThat(retrievedOffer.company()).isEqualTo("Tech Solutions")
        );

//        step 13: there are 2 new offers in external HTTP server
        
        // given && when && then
        
        wireMockServer.stubFor(WireMock.get("/offers")
                .inScenario("Offers scenario")
                .whenScenarioStateIs("Four Offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(retrieveFourOffersJson())));
        
        
//        step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
        
        // given && when
        List<OfferDto> nextTwoOfferDtosList = offerFacade.fetchAllOffersAndSaveIfNotExists();

        await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> offerRepository.findAll().size() > sizeWithExpectedTwoOffers
                        );

        log.info("Size after second fetching : "  +  offerRepository.findAll().size());

        //then

        assertAll(
                () -> assertThat(nextTwoOfferDtosList).hasSize(2),
                () -> assertThat(offerRepository.findAll()).hasSize(4)
        );


//        step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000

        // given && when

        MvcResult resultWithExpectedFourOffers = mockMvc.perform(get("/offers")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        String expectedFourOffersJson = resultWithExpectedFourOffers.getResponse().getContentAsString();
        List<OfferDto> mappedExpectedFourOffers = objectMapper.readValue(expectedFourOffersJson, new TypeReference<List<OfferDto>>() {
        });

        log.info("mappedExpectedFourOffers Size " + mappedExpectedFourOffers.size());

        //then
        OfferDto expectedIncluededOfferDto = OfferDto.builder()
                .offerUrl("https://nextgenapps.com/jobs/java-software-engineer")
                .company("NextGen Apps")
                .title("Java Software Engineer")
                .salary("7000 - 9000")
                .build();

        assertAll(
                () -> assertThat(mappedExpectedFourOffers)
                        .extracting(OfferDto::company, OfferDto::salary, OfferDto::title, OfferDto::offerUrl)
                        .contains(tuple(expectedIncluededOfferDto.company(), expectedIncluededOfferDto.salary(),
                                expectedIncluededOfferDto.title(), expectedIncluededOfferDto.offerUrl())),
                () -> assertThat(mappedExpectedFourOffers).hasSize(4),
                () -> assertThat(mappedExpectedFourOffers).isNotNull()
        );


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
        assertAll(
                () -> assertThat(offerDto).isNotNull(),
                () ->     assertThat(offerDto.offerUrl()).isEqualTo("https://new.com"),
                () -> assertThat(offerDto.salary()).isEqualTo("7000 - 10000"),
                () ->  assertThat(offerDto.company()).isEqualTo("new company")
        );



//        step 17: user made GET /offers/newOfferId with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 1 offer

        //given && when
        MvcResult returnedOffer = mockMvc.perform(get("/offers/" + offerId)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String returnedOfferJson = returnedOffer.getResponse().getContentAsString();
        OfferDto mappedReturnedOffer = objectMapper.readValue(returnedOfferJson, OfferDto.class);

        //then
        assertAll(
                () -> assertThat(mappedReturnedOffer.id()).isEqualTo(offerId),
                () -> assertThat(mappedReturnedOffer.company()).isEqualTo("new company"),
                () -> assertThat(mappedReturnedOffer.offerUrl()).isEqualTo("https://new.com"),
                () -> assertThat(mappedReturnedOffer.title()).isEqualTo("new offer")
        );




    }
}
