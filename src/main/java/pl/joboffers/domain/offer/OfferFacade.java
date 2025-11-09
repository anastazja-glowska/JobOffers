package pl.joboffers.domain.offer;

public class OfferFacade {

    public String findAllOffers(){
        return "all";
    }

    public String findOfferById(Long id){
        return "id";
    }

    public String saveOffer(String offer){
        return "offer";
    }
    public void fetchAllOffersAndSaveIfNotExists(){

    }
}
