package pl.joboffers.domain.offer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.joboffers.domain.offer.dto.OfferDto;
import pl.joboffers.domain.offer.dto.RemoteOfferDto;

import java.util.List;

@RequiredArgsConstructor
@Component
class OfferFetcher {
    
    private final OfferRepository offerRepository;
    private final RemoteOfferFetcher remoteOfferFetcher;
    
    List<OfferDto> fetchAllOffersAndSaveAllIfNotExists(){
        List<RemoteOfferDto> remoteOffers = remoteOfferFetcher.fetchOffersFromServer();

        List<RemoteOfferDto> newRemoteOffers = remoteOffers.stream()
                .filter(remoteOfferDto -> !remoteOfferDto.offerUrl().isEmpty())
                .filter(remoteOfferDto -> !offerRepository.existsOfferByOfferUrl(remoteOfferDto.offerUrl()))
                .toList();

        List<Offer> toSave = OfferMapper.mapFromRemoteOfferDtos(newRemoteOffers);

        offerRepository.saveAll(toSave);
        return OfferMapper.mapFromOffers(toSave);
    }
    
    
}
