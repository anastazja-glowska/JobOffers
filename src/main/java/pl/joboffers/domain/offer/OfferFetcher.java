package pl.joboffers.domain.offer;

import lombok.RequiredArgsConstructor;
import pl.joboffers.domain.offer.dto.OfferDto;
import pl.joboffers.domain.offer.dto.RemoteOfferDto;

import java.util.List;

@RequiredArgsConstructor
class OfferFetcher {
    
    private final OfferRepository offerRepository;
    private final RemoteOfferFetcher remoteOfferFetcher;
    
    List<OfferDto> fetchAllOffersAndSaveAllIfNotExists(){
        List<Offer> offers = offerRepository.findAll();

        if(offers == null ||offers.isEmpty()){
            List<RemoteOfferDto> remoteOfferDtos = remoteOfferFetcher.fetchOffersFromServer();
            List<Offer> offerList = OfferMapper.mapFromRemoteOfferDtos(remoteOfferDtos);
            offerRepository.saveAll(offerList);
            return OfferMapper.mapFromOffers(offerList);
        }
        return List.of();
    }
    
    
}
