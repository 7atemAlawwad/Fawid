package com.example.finalprojectjavabootcamp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity



public class CarListing {

    @Id
    private Integer id;

    @Column(columnDefinition = "varchar(30) not null")
    private String car_type; // suv, sedan... etc
    @Column(columnDefinition = "varchar(255) not null")
    private String make;
    @Column(columnDefinition = "varchar(255) not null")
    private String model;
    @Column(columnDefinition = "Integer not null")
    private Integer year;
    @Column(columnDefinition = "varchar(30) not null")
    private String color;
    @Column(columnDefinition = "varchar(8) not null")
    private String fuel_type;
    @Column(columnDefinition = "Integer not null")
    private Integer mileage;



    @OneToOne
    @MapsId
    @JsonIgnore
    private Listing listing;

}
