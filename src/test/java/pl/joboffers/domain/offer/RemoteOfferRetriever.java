package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.RemoteOfferDto;

import java.util.List;

class RemoteOfferRetriever implements RemoteOfferFetcher {


    @Override
    public List<RemoteOfferDto> fetchOffersFromServer() {
        return generateOffersForTests();
    }

    private List<RemoteOfferDto> generateOffersForTests(){

        RemoteOfferDto remoteOfferDto1 = RemoteOfferDto.builder()
                .id("001")
                .title("Title 1")
                .offerUrl("https://www.google1.com")
                .company("Company 1")
                .salary("10 000")
                .build();

        RemoteOfferDto remoteOfferDto2 = RemoteOfferDto.builder()
                .id("002")
                .title("Title 2")
                .offerUrl("https://www.google2.com")
                .company("Company 2")
                .salary("7 000")
                .build();

        RemoteOfferDto remoteOfferDto3 = RemoteOfferDto.builder()
                .id("003")
                .title("Title 3")
                .offerUrl("https://www.google3.com")
                .company("Company 3")
                .salary("8 000")
                .build();

        return List.of(remoteOfferDto1, remoteOfferDto2, remoteOfferDto3);
    }
}
