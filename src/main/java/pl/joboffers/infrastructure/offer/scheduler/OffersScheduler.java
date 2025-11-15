package pl.joboffers.infrastructure.offer.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.domain.offer.dto.OfferDto;

import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class OffersScheduler {

    private final OfferFacade  offerFacade;

    @Scheduled(cron = "${job.offers.scheduled.offers.occurrence}")
        public void scheduleOffers(){
        List<OfferDto> offerDtos = offerFacade.fetchAllOffersAndSaveIfNotExists();
        log.info("Offers : " + offerDtos);
    }
}
