package com.example.finalprojectjavabootcamp.DTOIN;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchCarDTOIn {

    @NotEmpty(message = "Car type is required")
    private String make;
    @NotEmpty(message = "Car type is required")
    private String model;
    @NotNull(message = "Year is required")
    private Integer year;
    @NotEmpty(message = "Color is required")
    @Pattern(regexp = "(?i)^(White|Black|Gray|Silver|Blue|Green|Red|Orange|Yellow|Brown|Beige|Purple|Pink|Gold|Other)$",message = "Color must be White, Black, Gray, Silver, Blue, Green, Red, Orange, Yellow, Brown, Beige, Purple, Pink, Gold or Other ")
    private String color;
    @NotEmpty(message = "Fuel type is required")
    @Pattern(regexp = "(?i)^(91|95|Diesel|Electric)$",message = "Fuel type must be 91, 95, Diesel or Electric")
    private String fuel_type;
    @PositiveOrZero(message = "Mileage must be a positive or zero number")
    @NotNull(message = "Mileage is required")
    private Integer mileage;
    private Boolean autoNegotiation;
    private Double price;
    private String notes;
}
