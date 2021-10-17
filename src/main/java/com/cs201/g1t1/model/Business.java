package com.cs201.g1t1.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Business {
    @Id
    @JsonProperty("business_id")
    private String businessId;

    private String name;

    private String address;

    private String city;

    private String state;

    @JsonProperty("postal_code")
    private String postalCode;

    private double longitude;

    private double latitude;

    private double stars;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "business_category", joinColumns = @JoinColumn(name = "business_id"), inverseJoinColumns = @JoinColumn(name = "category"))
    private Set<Category> categories;

}
