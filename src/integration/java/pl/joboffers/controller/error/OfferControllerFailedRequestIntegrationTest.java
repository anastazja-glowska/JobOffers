package pl.joboffers.controller.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.domain.offer.Offer;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.infrastructure.offer.controller.error.OfferAlreadyExistsExceptionResponseDto;
import pl.joboffers.infrastructure.offer.controller.error.OfferNotFoundResponseDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OfferControllerFailedRequestIntegrationTest extends BaseIntegrationTest {

    @Autowired
    OfferFacade offerFacade;

    @Test
    @DisplayName("Should return offer already exists exception when user try to save offer with existing offer url")
    void should_return_offer_already_exists_exception_when_user_try_to_save_offer_with_existing_offer_url() throws Exception {
        //given
        Offer offer = new Offer("Java Junior Developer", "Tech Solutions",
                "7000 - 9000", "https://new.com");

        offerFacade.saveOffer(offer);

        //when
        ResultActions performed = mockMvc.perform(post("/offers").content(
                """
                        {
                          "title": "Java Developer",
                          "company": "Java techs",
                          "salary": "8000 - 9000",
                          "offerUrl": "https://new.com"
                        }
                        """.trim()
        ).contentType(MediaType.APPLICATION_JSON));
        MvcResult result = performed.andExpect(status().isBadRequest()).andReturn();
        String jsonResult = result.getResponse().getContentAsString();
        OfferAlreadyExistsExceptionResponseDto exceptionResponseDto = objectMapper
                .readValue(jsonResult, OfferAlreadyExistsExceptionResponseDto.class);

        //then
        assertAll(
                () -> assertThat(exceptionResponseDto.message()).isEqualTo("Offer already exists!"),
                () -> assertThat(exceptionResponseDto.status()).isEqualTo(HttpStatus.BAD_REQUEST),
                () -> assertThat(exceptionResponseDto).isNotNull()
        );
    }




    @Test
    @DisplayName("Should return http status not found when user make get request with not existing offer id")
    void should_return_http_status_not_found_when_user_make_get_request_with_not_existing_offer_id() throws Exception {
        //given && when
        MvcResult result = mockMvc.perform(get("/offers/9999")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        OfferNotFoundResponseDto offerNotFoundResponse = objectMapper.readValue(jsonResult, OfferNotFoundResponseDto.class);

        //then
        assertAll(
                () -> assertThat(offerNotFoundResponse).isNotNull(),
                () -> assertThat(offerNotFoundResponse.message()).isEqualTo("Offer with offerUrl [9999] already exists!"),
                () -> assertThat(offerNotFoundResponse.status()).isEqualTo(HttpStatus.NOT_FOUND)
        );
    }

}
