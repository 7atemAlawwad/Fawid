package com.example.finalprojectjavabootcamp.Repository;

import com.example.finalprojectjavabootcamp.Model.CarListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CarListingRepository extends JpaRepository<CarListing,Integer> {
    List<CarListing> findCarListingsByColor(String color);
    List<CarListing> findCarListingsByMake(String make);
    List<CarListing> findCarListingsByModel(String model);
    List<CarListing> findCarListingsByYear(Integer year);
    @Query("SELECT c FROM CarListing c WHERE LOWER(c.fuel_type) = LOWER(:fuelType)")
    List<CarListing> findCarListingsByFuelType(String fuelType);
    @Query("SELECT c FROM CarListing c WHERE c.listing.city= :city")
    List<CarListing> findCarListingsByCity(String city);

    List<CarListing> findCarListingsByMileageLessThanEqual(Integer mileageIsLessThan);

    Object findCarListingById(Integer id);
}
