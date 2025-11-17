package pl.joboffers.domain.offer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.joboffers.domain.offer.dto.OfferDto;
import pl.joboffers.domain.offer.dto.RemoteOfferDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@Log4j2
class OfferFacadeTest {

    OfferRepository offerRepository = new OfferReposiotoryTestImpl();

    OfferRetriever offerRetriever = new OfferRetriever(offerRepository);
    OfferAdder offerAdder = new OfferAdder(offerRepository, offerRetriever);

    RemoteOfferFetcher remoteOfferFetcher = new RemoteOfferRetriever();
    OfferFetcher offerFetcher = new OfferFetcher(offerRepository, remoteOfferFetcher);


    OfferFacade offerFacade = new OfferConfiguration()
            .offerFacade(offerAdder, offerFetcher, offerRetriever);


    @Test
    @DisplayName("Should fetch all offers and save all If Not Exists")
    void should_fetch_All_Offers_And_save_All_If_Not_Exists() throws JsonProcessingException {


        //when
        List<OfferDto> result = offerFacade.fetchAllOffersAndSaveIfNotExists();

        //then
        log.info("OfferDtos size " + result.size());

        OfferDto expectedIncludedOffer = OfferDto.builder()
                .id("001")
                .title("Title 1")
                .offerUrl("https://www.google1.com")
                .company("Company 1")
                .salary("10 000")
                .build();

        assertThat(result).hasSize(3);
        assertThat(result).contains(expectedIncludedOffer);


    }

    @Test
    @DisplayName("Should fetch any offer and save any when offer repository contains all offers")
    void should_fetch_any_offer_and_save_any_when_offer_repository_contains_all_offers() throws JsonProcessingException {
        //given

        RemoteOfferRetriever remoteOfferRetriever = new RemoteOfferRetriever();
        List<RemoteOfferDto> remoteOfferDtos = remoteOfferRetriever.fetchOffersFromServer();
        List<Offer> offerList = OfferMapper.mapFromRemoteOfferDtos(remoteOfferDtos);

        log.info("RemoteOfferDtos size " + remoteOfferDtos.size());

//        offerRepository.saveAll(offerList);

        offerList.stream().forEach(offer -> offerRepository.save(offer));


        //when
        List<OfferDto> result = offerFacade.fetchAllOffersAndSaveIfNotExists();

        //then

        log.info("OfferDtos size " + result.size());
        assertThat(result).isEmpty();
        assertThat(remoteOfferDtos).hasSize(3);
    }

    @Test
    @DisplayName("Should save new offer when it does not exists in database")
    void should_save_new_offer_when_it_does_not_exists_in_database(){
        //given

        Offer offer = new Offer("001", "Title", "Company", "10 000",
                "https://www.google1.com");

        //when
        OfferDto offerDto = offerFacade.saveOffer(offer);

        //then

        OfferDto expectedOffer = OfferDto.builder()
                .id("001")
                .title("Title")
                .offerUrl("https://www.google1.com")
                .company("Company")
                .salary("10 000")
                .build();

        assertThat(offerDto).isEqualTo(expectedOffer);
        assertThat(offerDto).isNotNull();
        assertThat(offerDto.company()).isEqualTo(expectedOffer.company());
        assertThat(offerDto.salary()).isEqualTo(expectedOffer.salary());
        assertThat(offerDto.id()).isEqualTo(expectedOffer.id());

    }

    @Test
    @DisplayName("Should throw offer already exists exception when saved offer already exists")
    void should_throw_offer_already_exists_exception_when_saved_offer_already_exists(){

        //given

        Offer offer = new Offer("001", "Title", "Company", "10 000",
                "https://www.google1.com");

        offerRepository.save(offer);

        //when
        Throwable result = catchThrowable(() -> offerFacade.saveOffer(offer));

        //then
        assertThat(result).isInstanceOf(OfferAlreadyExistsException.class);
        assertThat(result.getMessage()).isEqualTo("Offer already exists");
    }

    @Test
    @DisplayName("Should find existing offer by id")
    void should_find_existing_offer_by_id(){
        //given

        Offer offer = new Offer("001", "Title", "Company", "10 000",
                "https://www.google1.com");

        offerRepository.save(offer);

        //when
        OfferDto result = offerFacade.findOfferById("001");


        //then
        OfferDto expectedOffer = OfferDto.builder()
                .id("001")
                .title("Title")
                .offerUrl("https://www.google1.com")
                .company("Company")
                .salary("10 000")
                .build();

        assertThat(result).isEqualTo(expectedOffer);
        assertThat(result).isNotNull();
        assertThat(result.company()).isEqualTo(expectedOffer.company());
        assertThat(result.salary()).isEqualTo(expectedOffer.salary());
        assertThat(result.id()).isEqualTo(expectedOffer.id());


    }

    @Test
    @DisplayName("Should retrieve all existing offers from database")
    void should_retrieve_all_existing_offers_from_database(){

        //given
        Offer offer = new Offer("004", "Title", "Company", "10 000",
                "https://www.google4.com");

        OfferDto saved = offerFacade.saveOffer(offer);

        log.info("Saved offer " + saved);


        RemoteOfferRetriever remoteOfferRetriever = new RemoteOfferRetriever();
        List<RemoteOfferDto> remoteOfferDtos = remoteOfferRetriever.fetchOffersFromServer();
        List<Offer> offerList = OfferMapper.mapFromRemoteOfferDtos(remoteOfferDtos);

        log.info("RemoteOfferDtos size " + remoteOfferDtos.size());

//        offerRepository.saveAll(offerList);
        offerList.stream().forEach(offer1 -> offerFacade.saveOffer(offer1));

        //when

        List<OfferDto> result = offerFacade.findAllOffers();

        //then


        OfferDto expectedOfferIncluded = OfferDto.builder()
                .id("004")
                .title("Title")
                .offerUrl("https://www.google4.com")
                .company("Company")
                .salary("10 000")
                .build();

        assertThat(result).hasSize(4);
        assertThat(result).contains(expectedOfferIncluded);

    }

    @Test
    @DisplayName("Should fetch only two offers when one offer already exists in database")
    void should_fetch_only_two_offers_when_one_offer_already_exists_in_database() throws JsonProcessingException {
        //given
        Offer offer = new Offer("001", "Title 1", "Company 1", "10 000",
                "https://www.google1.com");

        Offer saved = offerRepository.save(offer);

        log.info("Saved offer " + saved);


        //when

        List<OfferDto> result = offerFacade.fetchAllOffersAndSaveIfNotExists();

        //then

        OfferDto expectedOfferIncluded = OfferDto.builder()
                .id("002")
                .title("Title 2")
                .offerUrl("https://www.google2.com")
                .company("Company 2")
                .salary("7 000")
                .build();

        OfferDto expectedOfferIncluded2 = OfferDto.builder()
                .id("003")
                .title("Title 3")
                .offerUrl("https://www.google3.com")
                .company("Company 3")
                .salary("8 000")
                .build();

        assertThat(result).hasSize(2);
        assertThat(result).containsOnly(expectedOfferIncluded,expectedOfferIncluded2);

    }

    @Test
    @DisplayName("Should throw offer not found exception when offer with id not found")
    void should_throw_offer_not_found_exception_when_offer_with_id_not_found(){


        //when
        Throwable result = catchThrowable(() -> offerFacade.findOfferById("001"));

        //then
        assertThat(result).isInstanceOf(OfferNotFoundException.class);

    }
}