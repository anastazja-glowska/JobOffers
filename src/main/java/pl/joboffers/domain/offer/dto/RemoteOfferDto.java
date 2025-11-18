package pl.joboffers.domain.offer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record RemoteOfferDto(@JsonProperty("title") String title, @JsonProperty("company") String company,
                             @JsonProperty("salary") String salary, @JsonProperty("offerUrl") String offerUrl) {
}
