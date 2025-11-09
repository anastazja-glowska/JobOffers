package pl.joboffers.domain.offer;

import java.util.Optional;
import java.util.Set;

public interface OfferRepository {

    Offer save(Offer offer);

    Optional<Offer> findById(Long id);

    Set<Offer> findAll();
}
