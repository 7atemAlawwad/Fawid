package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.DTOIN.AiDTOIn;
import com.example.finalprojectjavabootcamp.DTOIN.SearchCarDTOIn;
import com.example.finalprojectjavabootcamp.DTOIN.SearchRealEstateDTOIn;
import com.example.finalprojectjavabootcamp.Model.*;


import com.example.finalprojectjavabootcamp.Repository.*;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;
    private final ResultRepository resultRepository;
    private final CarListingRepository carListingRepository;
    private final RealEstateListingRepository realEstateListingRepository;
    private final BuyerRepository buyerRepository;
    private final ListingRepository listingRepository;
    private final NegotiationService negotiationService;
    private final AiService aiService;

    public void CreateCarSearch(SearchCarDTOIn searchCarDTOIn, Integer buyerId) {
        Buyer buyer = buyerRepository.findBuyersById(buyerId);
        if (buyer == null) {
            throw new ApiException("Buyer not found");
        }

        List<CarListing> listings = carListingRepository.findAll();



        String searchQuery = String.format("%s %s %s",
                searchCarDTOIn.getMake() != null ? searchCarDTOIn.getMake() : "",
                searchCarDTOIn.getModel() != null ? searchCarDTOIn.getModel() : "",
                searchCarDTOIn.getYear() != null ? searchCarDTOIn.getYear() : ""
        ).trim();

        Search search = new Search(null,"car",searchQuery,searchCarDTOIn.getAutoNegotiation(),buyer,null);
        searchRepository.save(search);


        for (CarListing listing : listings) {
            Result result = new Result(null,listing.getId(),aiService.chat("deep_search_ranker_ar",toJson(searchCarDTOIn)+"\n"+toJson(carListingRepository.findCarListingById(listing.getId()))),search);
            if(result.getCompatability().equals("لا يوجد مطابقة")){
                continue;
            }
            resultRepository.save(result);
            if (searchCarDTOIn.getAutoNegotiation()){
                negotiationService.createAi(listing.getId(),buyerId,new AiDTOIn(searchCarDTOIn.getPrice(),searchCarDTOIn.getNotes()));
            }
        }
    }

    private List<CarListing> findCarListingsByCriteria(SearchCarDTOIn searchCarDTOIn) {
        List<CarListing> listings = carListingRepository.findAll();

        if (searchCarDTOIn.getColor() != null) {
            listings.retainAll(carListingRepository.findCarListingsByColor(searchCarDTOIn.getColor()));
        }

        if (searchCarDTOIn.getMake() != null) {
            listings.retainAll(carListingRepository.findCarListingsByMake(searchCarDTOIn.getMake()));
        }

        if (searchCarDTOIn.getModel() != null) {
            listings.retainAll(carListingRepository.findCarListingsByModel(searchCarDTOIn.getModel()));
        }

        if (searchCarDTOIn.getYear() != null) {
            listings.retainAll(carListingRepository.findCarListingsByYear(searchCarDTOIn.getYear()));
        }

        if (searchCarDTOIn.getFuel_type() != null) {
            listings.retainAll(carListingRepository.findCarListingsByFuelType(searchCarDTOIn.getFuel_type()));
        }

        if (searchCarDTOIn.getMileage() != null) {
            listings.retainAll(carListingRepository.findCarListingsByMileageLessThanEqual((searchCarDTOIn.getMileage())));
        }
        System.out.println(listings.size());

        return listings;
    }

    public void CreateRealEstateSearch(SearchRealEstateDTOIn searchRealEstateDTOIn, Integer buyerId) {
        Buyer buyer = buyerRepository.findBuyersById(buyerId);
        if (buyer == null) {
            throw new ApiException("Buyer not found");
        }

        List<RealEstateListing> listings = findRealEstateListingsByCriteria(searchRealEstateDTOIn);

        String searchQuery = String.format("%s in %s with %s",
                searchRealEstateDTOIn.getType() != null ? searchRealEstateDTOIn.getType() : "property",
                searchRealEstateDTOIn.getNeighborhood() != null ? searchRealEstateDTOIn.getNeighborhood() : "any area",
                searchRealEstateDTOIn.getRooms() != null ?
                        String.format("%d room%s",
                                searchRealEstateDTOIn.getRooms(),
                                searchRealEstateDTOIn.getRooms() == 1 ? "" : "s"
                        ) : "any rooms"
        ).trim();

        if (searchRealEstateDTOIn.getIsRental() != null) {
            searchQuery += searchRealEstateDTOIn.getIsRental() ? " (for rent)" : " (for sale)";
        }

        if (searchRealEstateDTOIn.getBathrooms() != null) {
            searchQuery += String.format(", %d bathroom%s",
                    searchRealEstateDTOIn.getBathrooms(),
                    searchRealEstateDTOIn.getBathrooms() == 1 ? "" : "s"
            );
        }

        if (searchRealEstateDTOIn.getSquareMeter() != null) {
            searchQuery += String.format(", %d sqm", searchRealEstateDTOIn.getSquareMeter());
        }

        Search search = new Search(null, "real_estate",searchQuery, searchRealEstateDTOIn.getAutoNegotiation(), buyer, null);
        searchRepository.save(search);

        for (RealEstateListing listing : listings) {
            Result result = new Result(null,listing.getId(),aiService.chat("deep_search_ranker_ar",toJson(searchRealEstateDTOIn)+"\n"+toJson(listing)),search);
            resultRepository.save(result);
            if (searchRealEstateDTOIn.getAutoNegotiation() && result.getCompatability().equalsIgnoreCase("مطابقة مرتفعة")) {
                negotiationService.createAi(listing.getId(), buyerId,
                        new AiDTOIn(searchRealEstateDTOIn.getPrice(), searchRealEstateDTOIn.getNotes()));
            }
        }
    }

    private List<RealEstateListing> findRealEstateListingsByCriteria(SearchRealEstateDTOIn searchRealEstateDTOIn) {
        List<RealEstateListing> listings = realEstateListingRepository.findAll();

        if (searchRealEstateDTOIn.getType() != null) {
            listings.retainAll(realEstateListingRepository.findRealEstateListingsByReal_estate_type(searchRealEstateDTOIn.getType()));
        }

        if (searchRealEstateDTOIn.getIsRental() != null) {
            listings.retainAll(realEstateListingRepository.findRealEstateListingsByIsRental(searchRealEstateDTOIn.getIsRental()));
        }

        if (searchRealEstateDTOIn.getRooms() != null) {
            listings.retainAll(realEstateListingRepository.findRealEstateListingsByRoomsGreaterThanEqual(searchRealEstateDTOIn.getRooms()));
        }

        if (searchRealEstateDTOIn.getBathrooms() != null) {
            listings.retainAll(realEstateListingRepository.findRealEstateListingsByBathroomsGreaterThanEqual(searchRealEstateDTOIn.getBathrooms()));
        }

        if (searchRealEstateDTOIn.getSquareMeter() != null) {
            listings.retainAll(realEstateListingRepository.findRealEstateListingsBySquareMeterGreaterThanEqual(searchRealEstateDTOIn.getSquareMeter()));
        }

        if (searchRealEstateDTOIn.getNeighborhood() != null) {
            listings.retainAll(realEstateListingRepository.findRealEstateListingsByNeighborhood(searchRealEstateDTOIn.getNeighborhood()));
        }

        return listings;
    }

    public List<Listing> getSearchResultsById(Integer SearchId, Integer userId){
        Search search = searchRepository.findSearchById(SearchId);
        if (search == null){
            throw new ApiException("Search not found");
        }
        Buyer buyer = buyerRepository.findBuyersById(userId);
        if (buyer == null || !search.getBuyer().getId().equals(search.getBuyer().getId())) {
            throw new ApiException("Buyer not found");
        }
        List<Result> results = resultRepository.findResultsBySearch(search);
        ArrayList<Listing> listings = new ArrayList<>();
        for (Result result : results){
            listings.add(listingRepository.getListingById(result.getListingId()));
        }
        return listings;
    }


    public String toJson(Object object) {
        JSONObject jsonObject = new JSONObject();
        if (object instanceof SearchCarDTOIn searchCar) {
            jsonObject.put("make", searchCar.getMake());
            jsonObject.put("model", searchCar.getModel());
            jsonObject.put("year", searchCar.getYear());
            jsonObject.put("color", searchCar.getColor());
            jsonObject.put("fuel_type", searchCar.getFuel_type());
            jsonObject.put("mileage", searchCar.getMileage());
            jsonObject.put("autoNegotiation", searchCar.getAutoNegotiation());
            jsonObject.put("price", searchCar.getPrice());
            jsonObject.put("notes", searchCar.getNotes());
        }
        if (object instanceof SearchRealEstateDTOIn searchRealEstate) {
            jsonObject.put("type", searchRealEstate.getType());
            jsonObject.put("isRental", searchRealEstate.getIsRental());
            jsonObject.put("rooms", searchRealEstate.getRooms());
            jsonObject.put("bathrooms", searchRealEstate.getBathrooms());
            jsonObject.put("squareMeter", searchRealEstate.getSquareMeter());
            jsonObject.put("neighborhood", searchRealEstate.getNeighborhood());
            jsonObject.put("autoNegotiation", searchRealEstate.getAutoNegotiation());
            jsonObject.put("price", searchRealEstate.getPrice());
            jsonObject.put("notes", searchRealEstate.getNotes());
        }
        if (object instanceof CarListing carListing) {
            jsonObject.put("id", carListing.getId());
            jsonObject.put("car_type", carListing.getCar_type());
            jsonObject.put("make", carListing.getMake());
            jsonObject.put("model", carListing.getModel());
            jsonObject.put("year", carListing.getYear());
            jsonObject.put("color", carListing.getColor());
            jsonObject.put("fuel_type", carListing.getFuel_type());
            jsonObject.put("mileage", carListing.getMileage());

            if (carListing.getListing() != null) {
                JSONObject listingInfo = new JSONObject();
                Listing listing = carListing.getListing();
                listingInfo.put("id", listing.getId());
                listingInfo.put("title", listing.getTitle());
                listingInfo.put("description", listing.getDescription());
                listingInfo.put("status", listing.getStatus());
                listingInfo.put("least_price", listing.getLeast_price());
                listingInfo.put("city", listing.getCity());
                listingInfo.put("created_at", listing.getCreated_at());
                listingInfo.put("updated_at", listing.getUpdated_at());
                jsonObject.put("listing_details", listingInfo);
            }
        }
        if (object instanceof RealEstateListing realEstateListing) {
            jsonObject.put("id", realEstateListing.getId());
            jsonObject.put("real_estate_type", realEstateListing.getReal_estate_type());
            jsonObject.put("isRental", realEstateListing.getIsRental());
            jsonObject.put("rooms", realEstateListing.getRooms());
            jsonObject.put("bathrooms", realEstateListing.getBathrooms());
            jsonObject.put("squareMeter", realEstateListing.getSquareMeter());
            jsonObject.put("city", realEstateListing.getCity());
            jsonObject.put("neighborhood", realEstateListing.getNeighborhood());

            if (realEstateListing.getListing() != null) {
                JSONObject listingInfo = new JSONObject();
                Listing listing = realEstateListing.getListing();
                listingInfo.put("id", listing.getId());
                listingInfo.put("title", listing.getTitle());
                listingInfo.put("description", listing.getDescription());
                listingInfo.put("status", listing.getStatus());
                listingInfo.put("least_price", listing.getLeast_price());
                listingInfo.put("city", listing.getCity());
                listingInfo.put("created_at", listing.getCreated_at());
                listingInfo.put("updated_at", listing.getUpdated_at());
                jsonObject.put("listing_details", listingInfo);
            }
        }
        return jsonObject.toString();
    }

}
