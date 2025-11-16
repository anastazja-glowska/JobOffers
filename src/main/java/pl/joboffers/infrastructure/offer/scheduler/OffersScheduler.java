package pl.joboffers.infrastructure.offer.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.domain.offer.dto.OfferDto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class OffersScheduler {

    private final OfferFacade  offerFacade;

    private static final String ADDED_RETRIEVED_OFFERS_INFO = "New offers {} were added!";
    private static final String STARTED_INFO = "Started getting offers {}";
    private static final String FINISHED_INFO = "Finished getting offers {}";
    private static final SimpleDateFormat dateFormat= new SimpleDateFormat("HH:mm:ss");



//    @Scheduled(fixedDelayString = "${job.offers.scheduled.offers.occurrence.delay}")
//        public List<OfferDto> scheduleOffers(){
//
//        List<OfferDto> offerDtos = offerFacade.fetchAllOffersAndSaveIfNotExists();
//

//        log.info(FINISHED_INFO, dateFormat.format(new Date()));
//        return  offerDtos;
//
//    }


    @Scheduled(fixedDelayString = "${job.offers.scheduled.offers.occurrence}")
    public List<OfferDto> scheduleOffers(){
        log.info(STARTED_INFO, dateFormat.format(new Date()));
        List<OfferDto> offerDtos = offerFacade.fetchAllOffersAndSaveIfNotExists();
        log.info(ADDED_RETRIEVED_OFFERS_INFO, offerDtos.size());
        log.info(FINISHED_INFO, dateFormat.format(new Date()));
        return  offerDtos;
    }
}
