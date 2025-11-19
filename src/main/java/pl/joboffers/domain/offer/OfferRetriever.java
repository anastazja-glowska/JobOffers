package pl.joboffers.domain.offer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.joboffers.domain.offer.dto.OfferDto;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
class OfferRetriever {

    private final OfferRepository offerRepository;


    OfferDto findOfferById(String id) {

        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException(String.format("Offer with offerUrl [%s] already exists!", id )));

        return OfferMapper.mapFromOffer(offer);

    }

    List<OfferDto> findAllOffers(){

        List<Offer> offers = offerRepository.findAll();
        return OfferMapper.mapFromOffers(offers);

    }

    boolean existsOfferByOfferUrl(String offerUrl){
        return offerRepository.existsOfferByOfferUrl(offerUrl);
    }


}
