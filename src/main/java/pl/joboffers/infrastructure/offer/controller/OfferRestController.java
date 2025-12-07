package pl.joboffers.infrastructure.offer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.joboffers.domain.offer.Offer;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.domain.offer.dto.OfferDto;
import pl.joboffers.infrastructure.offer.controller.dto.OfferRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/offers")
public class OfferRestController {

    private final OfferFacade offerFacade;

    @GetMapping
    public ResponseEntity<List<OfferDto>> fetchOffers() {
        List<OfferDto> allOffers = offerFacade.findAllOffers();
        log.info("Retrieved {} offers", allOffers.size());
        return ResponseEntity.ok(allOffers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDto> fetchOffer(@PathVariable String id) {
        OfferDto offerById = offerFacade.findOfferById(id);
        log.debug("Fetching offer with id {}", id);
        return ResponseEntity.ok(offerById);
    }

    @PostMapping
    public ResponseEntity<OfferDto> createOffer(@RequestBody @Valid OfferRequestDto request) {
        Offer toSave = new Offer(request.title(), request.company(), request.salary(), request.offerUrl());
        OfferDto offerDto = offerFacade.saveOffer(toSave);
        return ResponseEntity.status(HttpStatus.CREATED).body(offerDto);

    }
}
