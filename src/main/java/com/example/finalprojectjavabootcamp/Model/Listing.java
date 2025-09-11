package com.example.finalprojectjavabootcamp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "LOWER(type) IN ('car','real_estate') AND " +
                      "LOWER(status) IN ('listed','closed','abandoned','deleted')"
)public class Listing {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(30) not null")
    private String title;
    @Column(columnDefinition = "varchar(300) not null")
    private String description;
    @Column(columnDefinition = "varchar(11) not null")
    private String type;
    @Column(columnDefinition = "varchar(11) not null")
    private String status;
    @Column(columnDefinition = "Double")
    private Double least_price;
    @Column(columnDefinition = "varchar(30) not null")
    private String city;

    @ManyToOne
    @JsonIgnore
    private Seller seller;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "listing")
    private Set<Negotiation> negotiation;


    @OneToOne(cascade = CascadeType.ALL, mappedBy = "listing")
    @PrimaryKeyJoinColumn
    private CarListing carListing;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "listing")
    @PrimaryKeyJoinColumn
    private RealEstateListing realEstateListing;

    @CreationTimestamp
    @Column(columnDefinition = "timestamp")
    private LocalDateTime created_at;
    @UpdateTimestamp
    @Column(columnDefinition = "timestamp")
    private LocalDateTime updated_at;
}
