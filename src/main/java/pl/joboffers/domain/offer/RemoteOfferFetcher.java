package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.RemoteOfferDto;

import java.util.List;

public interface RemoteOfferFetcher {

    List<RemoteOfferDto> fetchOffersFromServer();
}
