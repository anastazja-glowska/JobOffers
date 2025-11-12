package pl.joboffers.domain.offer;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.joboffers.domain.offer.dto.OfferDto;

@RequiredArgsConstructor
@Component
class OfferAdder {

    private final OfferRepository offerRepository;
    private final OfferRetriever offerRetriever;

    OfferDto saveOffer(Offer offer){
        if(offerRetriever.existsOfferByOfferUrl(offer.getOfferUrl())){
            throw new OfferAlreadyExistsException("Offer already exists");
        }

        Offer saved = offerRepository.save(offer);
        return  OfferMapper.mapFromOffer(saved);
    }
}
