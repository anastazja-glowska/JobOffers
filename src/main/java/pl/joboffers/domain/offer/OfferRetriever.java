package pl.joboffers.domain.offer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.joboffers.domain.offer.dto.OfferDto;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Log4j2
class OfferRetriever {

    private static final String OFFER_URL = "Retrieved offer url from repository: ";

    private final OfferRepository offerRepository;


    OfferDto findOfferById(String id) {

        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException(String.format("Offer with offerUrl [%s] does not exists!", id )));

        log.info(OFFER_URL + offer.getOfferUrl());
        return OfferMapper.mapFromOffer(offer);

    }

    List<OfferDto> findAllOffers() {

        List<Offer> offers = offerRepository.findAll();
        return OfferMapper.mapFromOffers(offers);

    }

    boolean existsOfferByOfferUrl(String offerUrl){
        return offerRepository.existsOfferByOfferUrl(offerUrl);
    }


}
