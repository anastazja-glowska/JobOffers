package pl.joboffers.domain.offer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OfferConfiguration {

//    @Bean
//    RemoteOfferFetcher remoteOfferFetcher() {
//        return new RemoteOfferRetriever();;
//    }

    @Bean
    OfferFacade offerFacade(OfferAdder offerAdder, OfferFetcher offerFetcher,
                            OfferRetriever offerRetriever) {



        return new OfferFacade(offerAdder, offerFetcher, offerRetriever);
    }
}
