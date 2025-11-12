package pl.joboffers.domain.offer;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OfferRepository extends MongoRepository<Offer,String> {

    boolean existsOfferByOfferUrl(String offerUrl);

    List<Offer> findAll();
}
