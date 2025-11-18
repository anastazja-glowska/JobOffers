package pl.joboffers.apivalidationerror;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.infrastructure.apivalidation.ApiValidationErrorsDto;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiValidationFailedTest extends BaseIntegrationTest {


    @Test
    @DisplayName("Should return status bad request and message when user gave offer with empty title and salary")
    void should_return_status_bad_request_and_message_when_user_gave_empty_offer() throws Exception {
        //given && when
        ResultActions resultActions = mockMvc.perform(post("/offers").content(
                """
                        {
                          "title": "",
                          "company": "string",
                          "salary": "string",
                          "offerUrl": "string"
                        }
                        """
        ).contentType(MediaType.APPLICATION_JSON));

        MvcResult result = resultActions.andExpect(status().isBadRequest()).andReturn();
        String json = result.getResponse().getContentAsString();
        ApiValidationErrorsDto errorsDto = objectMapper.readValue(json, ApiValidationErrorsDto.class);

        //then
        assertAll(
                () -> assertThat(errorsDto.messages()).containsExactlyInAnyOrder("title must not be empty")
        );
    }

    @Test
    void should_return_status_bad_request_and_message_when_user_does_not_provide_any_offer(){

    }




}
