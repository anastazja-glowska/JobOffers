package pl.joboffers.infrastructure.offer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.domain.offer.dto.OfferDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/offers")
public class OfferController {

    private final OfferFacade offerFacade;

    @GetMapping
    public ResponseEntity<List<OfferDto>> fetchOffers() {
        List<OfferDto> allOffers = offerFacade.findAllOffers();
        log.info("All retrieved offers : " + allOffers);
        return ResponseEntity.ok(allOffers);
    }

    @GetMapping("{id}")
    public ResponseEntity<OfferDto> fetchOffer(@PathVariable String id){
        OfferDto offerById = offerFacade.findOfferById(id);
        return ResponseEntity.ok(offerById);
    }
}
