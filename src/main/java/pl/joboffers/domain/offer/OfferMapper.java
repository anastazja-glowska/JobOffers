package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.OfferDto;
import pl.joboffers.domain.offer.dto.RemoteOfferDto;

import java.util.List;

class OfferMapper {

    static OfferDto mapFromOffer(Offer offer) {
        return OfferDto.builder()
                .id(offer.getId())
                .company(offer.getCompany())
                .offerUrl(offer.getOfferUrl())
                .title(offer.getTitle())
                .salary(offer.getSalary())
                .build();
    }

    static List<OfferDto> mapFromOffers(List<Offer> offers) {
        return offers.stream()
                .map(OfferMapper::mapFromOffer)
                .toList();
    }

    static List<Offer> mapFromRemoteOfferDtos(List<RemoteOfferDto> remoteOfferDtos) {
        return remoteOfferDtos.stream()
                .map(remoteOfferDto -> new Offer(
                        remoteOfferDto.id(), remoteOfferDto.title(), remoteOfferDto.company(),
                        remoteOfferDto.salary(), remoteOfferDto.offerUrl()
                )).toList();
    }
}
