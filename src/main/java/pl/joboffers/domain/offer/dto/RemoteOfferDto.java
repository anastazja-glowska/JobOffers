package pl.joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record RemoteOfferDto(String id, String title, String company, String salary, String offerUrl) {
}
