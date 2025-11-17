package pl.joboffers.infrastructure.offer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.domain.offer.dto.OfferDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class OfferController {

    private final OfferFacade offerFacade;

    @GetMapping("/offers")
    public ResponseEntity<List<OfferDto>> fetchOffers() throws JsonProcessingException {
        List<OfferDto> allOffers = offerFacade.findAllOffers();
        log.info("All retrieved offers : " + allOffers);
        return ResponseEntity.ok(allOffers);
    }
}
