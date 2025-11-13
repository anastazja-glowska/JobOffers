package pl.joboffers.domain.offer;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pl.joboffers.domain.offer.dto.RemoteOfferDto;

import java.util.List;


public interface RemoteOfferFetcher {

    List<RemoteOfferDto> fetchOffersFromServer();
}
