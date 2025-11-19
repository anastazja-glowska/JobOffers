package pl.joboffers.apivalidationerror;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.infrastructure.apivalidation.ApiValidationErrorsDto;
import pl.joboffers.infrastructure.offer.controller.error.OfferNotFoundResponse;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
public class ApiValidationFailedTest extends BaseIntegrationTest {


    @Test
    @DisplayName("Should return status bad request and message when user gave offer with empty title and salary")
    void should_return_status_bad_request_and_message_when_user_gave_empty_title_and_salary() throws Exception {
        //given && when
        ResultActions resultActions = mockMvc.perform(post("/offers").content(
                """
                        {
                          "title": "",
                          "company": "string",
                          "salary": "",
                          "offerUrl": "string"
                        }
                        """.trim()
        ).contentType(MediaType.APPLICATION_JSON));

        MvcResult result = resultActions.andExpect(status().isBadRequest()).andReturn();
        String json = result.getResponse().getContentAsString();
        ApiValidationErrorsDto errorsDto = objectMapper.readValue(json, ApiValidationErrorsDto.class);

        //then
        assertAll(
                () -> assertThat(errorsDto.messages()).containsExactlyInAnyOrder("title must not be empty",
                        "salary must not be empty"),
                () -> assertThat(errorsDto).isNotNull()
        );
    }

    @Test
    void should_return_status_bad_request_and_message_when_user_does_not_provide_any_offer() throws Exception {

        //given && when
        ResultActions performed = mockMvc.perform(post("/offers").content(
                """
                        {}
                        """.trim()
        ).contentType(MediaType.APPLICATION_JSON));

        MvcResult result = performed.andExpect(status().isBadRequest()).andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        ApiValidationErrorsDto errorsDto = objectMapper.readValue(jsonResponse, ApiValidationErrorsDto.class);
        log.info("Errors dto messages " + errorsDto.messages());

        //then
        assertAll(
                () -> assertThat(errorsDto.messages()).containsExactlyInAnyOrder("salary must not be null",
                        "title must not be empty","company must not be null",
                        "title must not be null","offerUrl must not be empty",
                        "salary must not be empty","offerUrl must not be null","company must not be empty"),
                () -> assertThat(errorsDto).isNotNull()
        );



    }

    @Test
    @DisplayName("Should return status bad request and message when user gave empty company and offer url")
    void should_return_status_bad_request_and_message_when_user_gave_empty_company_and_offer_url() throws Exception {
        //given && when
        ResultActions performed = mockMvc.perform(post("/offers").content(
                """
                        {
                          "title": "new offer",
                          "company": "",
                          "salary": "7000 - 9000"
                       }
                        """.trim()
        ).contentType(MediaType.APPLICATION_JSON));

        MvcResult result = performed.andExpect(status().isBadRequest()).andReturn();
        String jsonResult = result.getResponse().getContentAsString();
        ApiValidationErrorsDto errorsDto = objectMapper.readValue(jsonResult, ApiValidationErrorsDto.class);

        //then
        assertAll(
                () -> assertThat(errorsDto.messages()).containsExactlyInAnyOrder("offerUrl must not be empty",
                        "company must not be empty", "offerUrl must not be null"),
                () -> assertThat(errorsDto).isNotNull()
        );
    }

    @Test
    void should_return_http_status_not_found_when_user_make_get_request_with_not_existing_offer_id() throws Exception {
        //given && when
        MvcResult result = mockMvc.perform(get("/offers/9999")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        OfferNotFoundResponse offerNotFoundResponse = objectMapper.readValue(jsonResult, OfferNotFoundResponse.class);

        //then
        assertAll(
                () -> assertThat(offerNotFoundResponse).isNotNull(),
                () -> assertThat(offerNotFoundResponse.message()).isEqualTo("Offer not found for id 9999"),
                () -> assertThat(offerNotFoundResponse.status()).isEqualTo(HttpStatus.NOT_FOUND)
        );
    }




}
