package pl.joboffers.domain.offer;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class OfferReposiotoryTestImpl implements OfferRepository {

    Map<String, Offer> offersDatabase = new ConcurrentHashMap<>();




    @Override
    public boolean existsOfferByOfferUrl(String offerUrl) {
        return offersDatabase.values().stream()
                .anyMatch(offer -> offer.getOfferUrl().equals(offerUrl));
    }


    @Override
    public List<Offer> findAll() {
        return  offersDatabase.values().stream().collect(Collectors.toList());
    }



    @Override
    public <S extends Offer> List<S> findAll(Example<S> example) {
        return offersDatabase.values().stream()
                .map(offer -> (S) offer)
                .toList();
    }


    @Override
    public Offer  save(Offer offer) {
        if(offersDatabase.values().stream().anyMatch(offer1 -> offer1.getOfferUrl().equals(offer.getOfferUrl()))) {
            throw new OfferAlreadyExistsException("Offer with this url already exists");
        }

        if(offer.getId() == null){
            String id = UUID.randomUUID().toString();

            Offer savedOffer = new Offer(id, offer.getTitle(), offer.getCompany(), offer.getSalary(), offer.getOfferUrl());

            offersDatabase.put(savedOffer.getId(), savedOffer);
            return savedOffer;
        }

        offersDatabase.put(offer.getId(), offer);
        return offer;


    }



    @Override
    public <S extends Offer> List<S> saveAll(Iterable<S> entities) {
        Stream<S> stream = StreamSupport.stream(entities.spliterator(), false);
        List<S> list = stream.toList();
        list.forEach(offer -> {
            if(offer.getId() == null){
                offer.setId(UUID.randomUUID().toString());
            }
            offersDatabase.put(offer.getId(), offer);
        } );
        return list;
    }


    @Override
    public Optional<Offer> findById(String id) {
        return Optional.ofNullable(offersDatabase.get(id));
    }

    @Override
    public <S extends Offer> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> insert(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public <S extends Offer> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }



    @Override
    public <S extends Offer> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Offer> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Offer> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Offer> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Offer, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }






    @Override
    public boolean existsById(String s) {
        return false;
    }



    @Override
    public List<Offer> findAllById(Iterable<String> strings) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Offer entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Offer> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Offer> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Offer> findAll(Pageable pageable) {
        return null;
    }
}
