package com.example.finalprojectjavabootcamp.DTOIN;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRealEstateDTOIn {


    @NotEmpty(message = "Real estate type is required")
    @Pattern(regexp = "(?i)^(Apartment|Villa|Townhouse|Duplex|Studio|Penthouse|Store|Farm|Land|Other)$", message = "Real estate type must be Apartment, Villa, Townhouse, Duplex, Studio, Penthouse, Store, Farm, Land or Other")
    private String type;
    @NotNull(message = "isRental is required")
    private Boolean isRental;
    @Positive(message = "Rooms must be positive")
    private Integer rooms;
    @PositiveOrZero(message = "Bathrooms must be zero or positive")
    private Integer bathrooms;
    @Positive(message = "Square meter must be positive")
    private Integer squareMeter;
    private String neighborhood;
    private Boolean autoNegotiation;
    private Double price;
    private String notes;
}
