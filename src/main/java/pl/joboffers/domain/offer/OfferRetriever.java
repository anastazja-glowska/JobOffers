package pl.joboffers.domain.offer;

import lombok.RequiredArgsConstructor;
import pl.joboffers.domain.offer.dto.OfferDto;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
class OfferRetriever {

    private final OfferRepository offerRepository;


    OfferDto findOfferById(String id) {

        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException("Offer not found!"));

        return OfferMapper.mapFromOffer(offer);

    }

    List<OfferDto> findAllOffers(){

        List<Offer> offers = offerRepository.findAll();
        return OfferMapper.mapFromOffers(offers);

    }

    boolean existsOfferByOfferUrl(String offerUrl){
        return offerRepository.existsOfferByOfferUrl(offerUrl);
    }

//    boolean existsOfferById(Long id){
//        return offerRepository.existsById(id);
//    }
}
