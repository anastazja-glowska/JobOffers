package pl.joboffers.domain.offer;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import pl.joboffers.domain.offer.dto.OfferDto;

import java.util.List;

@RequiredArgsConstructor
public class OfferFacade {

    private final OfferAdder offerAdder;
    private final OfferFetcher offerFetcher;
    private final OfferRetriever offerRetriever;

    public List<OfferDto> findAllOffers() {
        return offerRetriever.findAllOffers();
    }

    public OfferDto findOfferById(String id){
        return offerRetriever.findOfferById(id);
    }

    public OfferDto saveOffer(Offer offer){
        return offerAdder.saveOffer(offer);
    }


    public List<OfferDto> fetchAllOffersAndSaveIfNotExists() throws JsonProcessingException {
        return offerFetcher.fetchAllOffersAndSaveAllIfNotExists();
    }
}
