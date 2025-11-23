package pl.joboffers.domain.offer;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.joboffers.domain.offer.dto.OfferDto;

@RequiredArgsConstructor
@Component
class OfferAdder {

    private static final String OFFER_INFO = "Offer already exists!";

    private final OfferRepository offerRepository;
    private final OfferRetriever offerRetriever;

    OfferDto saveOffer(Offer offer){
        if(offerRetriever.existsOfferByOfferUrl(offer.getOfferUrl())){
            throw new OfferAlreadyExistsException(OFFER_INFO);
        }

        Offer saved = offerRepository.save(offer);
        return  OfferMapper.mapFromOffer(saved);
    }
}
