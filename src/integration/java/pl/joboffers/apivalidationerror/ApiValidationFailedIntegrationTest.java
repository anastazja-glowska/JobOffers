package pl.joboffers.apivalidationerror;

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
import pl.joboffers.domain.offer.Offer;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.infrastructure.apivalidation.ApiValidationErrorsDto;
import pl.joboffers.infrastructure.offer.controller.error.OfferAlreadyExistsExceptionResponseDto;
import pl.joboffers.infrastructure.offer.controller.error.OfferNotFoundResponseDto;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
public class ApiValidationFailedIntegrationTest extends BaseIntegrationTest {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    public static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }


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
                () -> assertThat(errorsDto).isNotNull(),
                () -> assertThat(errorsDto.status()).isEqualTo(HttpStatus.BAD_REQUEST)
        );
    }

    @Test
    @DisplayName("Should return status bad request and message when user does not provide any offer")
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
                () -> assertThat(errorsDto).isNotNull(),
                () -> assertThat(errorsDto.status()).isEqualTo(HttpStatus.BAD_REQUEST)
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
                () -> assertThat(errorsDto).isNotNull(),
                () -> assertThat(errorsDto.status()).isEqualTo(HttpStatus.BAD_REQUEST)
        );
    }

    @Test
    @DisplayName("Should return status bad request 400 and validation message when user provide empty password and invalid email")
    void should_return_status_bad_request_400_and_validation_message_when_user_provide_empty_password_and_invalid_email() throws Exception {

        //given && when

        ResultActions performedInvalidCredentials = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username" : "email",
                        "password" : ""
                        }
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON));

        MvcResult result = performedInvalidCredentials.andExpect(status().isBadRequest()).andReturn();
        String badRequestResponseJson = result.getResponse().getContentAsString();
        ApiValidationErrorsDto errors = objectMapper.readValue(badRequestResponseJson, ApiValidationErrorsDto.class);

        //then

        assertAll(
                () -> assertThat(errors.messages()).containsExactlyInAnyOrder(
                        "password must have min 6 length size",
                        "email must have correct format", "email must not be empty"),
                () -> assertThat(errors.status()).isEqualTo(HttpStatus.BAD_REQUEST),
                () -> assertThat(errors).isNotNull()

        );


    }


    @Test
    @DisplayName("Should return status bad request 400 and validation message when user provide any email and invalid password")
    void should_return_status_bad_request_400_and_validation_message_when_user_provide_any_email_and_invalid_password() throws Exception {

        //given && when

        ResultActions performedInvalidCredentials = mockMvc.perform(post("/register")
                .content("""
                        {
                        "password" : "1234"
                        }
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON));

        MvcResult result = performedInvalidCredentials.andExpect(status().isBadRequest()).andReturn();
        String badRequestResponseJson = result.getResponse().getContentAsString();
        ApiValidationErrorsDto errors = objectMapper.readValue(badRequestResponseJson, ApiValidationErrorsDto.class);

        //then

        assertAll(
                () -> assertThat(errors.messages()).containsExactlyInAnyOrder(
                        "password must have min 6 length size",
                        "email must not be empty", "email must not be null"),
                () -> assertThat(errors.status()).isEqualTo(HttpStatus.BAD_REQUEST),
                () -> assertThat(errors).isNotNull()

        );


    }






}
