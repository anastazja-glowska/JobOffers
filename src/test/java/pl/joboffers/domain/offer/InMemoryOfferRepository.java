package pl.joboffers.domain.offer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class InMemoryOfferRepository implements OfferRepository {

    Map<Long, Offer> offersMap = new ConcurrentHashMap<>();

    @Override
    public Offer save(Offer offer) {
        return null;
    }

    @Override
    public Optional<Offer> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Set<Offer> findAll() {
        return Set.of();
    }
}
