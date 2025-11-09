package pl.joboffers.domain.offer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfferFacadeTest {

    OfferFacade offerFacade = new OfferFacade();


    @Test
    void should_add_4_offers_when_there_is_no_offers_in_database(){

    }

    @Test
    void should_throw_duplicate_key_exception_when_offer_with_url_exist(){

    }

    @Test
    void should_throw_not_found_exception_when_offer_not_found(){

    }

    @Test
    void should_fetch_offers_from_remote_and_save_all_when_repository_is_empty(){

    }

    @Test
    void should_find_offer_by_id_when_offer_was_saved(){

    }
}