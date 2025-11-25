package pl.joboffers.cache.redis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.WireMockJobOffersResponse;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.infrastructure.loginandregister.controller.dto.JwtResponseDto;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RedisOffersCacheIntegrationTest extends BaseIntegrationTest implements WireMockJobOffersResponse {


    private static final String OFFERS_ENDPOINT = "/offers";
    private static final String REGISTER_ENDPOINT = "/register";
    private static final String TOKEN_ENDPOINT = "/token";
    private static final String AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_BEARER = "Bearer ";


    @Container
    private static final GenericContainer<?> REDIS;

    @SpyBean
    OfferFacade offerFacade;

    @Autowired
    CacheManager cacheManager;

    static {
        REDIS = new GenericContainer<>("redis").withExposedPorts(6379);
        REDIS.start();
    }

    @DynamicPropertySource
    public static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.data.redis.port", () -> REDIS.getFirstMappedPort().toString() );
        registry.add("spring.cache.type", () -> "redis");
        registry.add("spring.cache.redis.time-to-live", () -> "PT1S");
    }

    @Test
    @DisplayName("Should save offers to cache and then invalidate by time to live")
    void should_save_offers_to_cache_and_then_invalidate_by_time_to_live() throws Exception {
        // step 1: someUser was registered with somePassword

        //given && when
        ResultActions registeredAction = mockMvc.perform(post(REGISTER_ENDPOINT)
                .content(retrieveSomeUserWithSomePassword())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        registeredAction.andExpect(status().isCreated());


        // step 2: user log in

        //given && when
        ResultActions performedToken = mockMvc.perform(post(TOKEN_ENDPOINT)
                .content(retrieveSomeUserWithSomePassword())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        MvcResult returnedResultWithToken = performedToken.andExpect(status().isOk()).andReturn();
        String resultJson = returnedResultWithToken.getResponse().getContentAsString();
        JwtResponseDto jwtResponseDto = objectMapper.readValue(resultJson, JwtResponseDto.class);
        String token = jwtResponseDto.token();

        // step 3: should save to cache offers request

        //given && when
        mockMvc.perform(get(OFFERS_ENDPOINT)
                .header(AUTHORIZATION, AUTHORIZATION_BEARER + token)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        verify(offerFacade, times(1)).findAllOffers();
        assertThat(cacheManager.getCacheNames().contains("jobOffers")).isTrue();

        // step 4: cache should be invalidated
        //given && when && then
        await()
                .atMost(Duration.ofSeconds(4))
                .pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                    mockMvc.perform(get(OFFERS_ENDPOINT)
                            .header(AUTHORIZATION, AUTHORIZATION_BEARER + token)
                            .contentType(MediaType.APPLICATION_JSON));

                    verify(offerFacade, atLeast(2)).findAllOffers();
                });

    }


}
