package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.Model.Listing;
import com.example.finalprojectjavabootcamp.Model.Seller;
import com.example.finalprojectjavabootcamp.Repository.ListingRepository;
import com.example.finalprojectjavabootcamp.Repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;
    private final SellerRepository sellerRepository;

    public List<Listing> getAllListings(){
        return listingRepository.findAll();
    }

    public List<Listing> getListingBySellerId(Integer sellerId){
        Seller seller = sellerRepository.findSellerById(sellerId);
        if (seller == null){
            throw new ApiException("Seller not found");
        }
        return listingRepository.findListingsBySeller(seller);
    }
    //search My Listings by date + status + type + query
    public List<Listing> getListingByStatus(String status){
        return listingRepository.findListingsByStatus(status);
    }

    public List<Listing> getListingByType(String type){
        if (!type.equalsIgnoreCase("all") && !type.equalsIgnoreCase("car") && !type.equalsIgnoreCase("real_estate")){
            throw new ApiException("Invalid type");
        }
        if ("all".equalsIgnoreCase(type)) {
            return listingRepository.findAll();
        }

        return listingRepository.findListingsByType(type);
    }

    public List<Listing> searchListing(String query){
        return listingRepository.searchListings(query);
    }

    public Object getListingById(Integer id){
        Listing listing = listingRepository.getListingById(id);
        if (listing == null){
            throw new ApiException("Listing not found");
        }
        if (listing.getType().equalsIgnoreCase("car")){
            return listing.getCarListing();
        }
        return listing.getRealEstateListing();
    }

    public void deleteListing(Integer id){
        Listing listing = listingRepository.getListingById(id);
        if (listing == null){
            throw new ApiException("Listing not found");
        }
        listingRepository.delete(listing);
    }


}
