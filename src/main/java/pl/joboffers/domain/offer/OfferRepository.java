package pl.joboffers.domain.offer;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface OfferRepository extends MongoRepository<Offer,String> {

    boolean existsOfferByOfferUrl(String offerUrl);

    List<Offer> findAll();
}
