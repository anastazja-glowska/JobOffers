package pl.joboffers.domain.offer.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record OfferDto(String id, String title, String company, String salary, String offerUrl) implements Serializable {
}
