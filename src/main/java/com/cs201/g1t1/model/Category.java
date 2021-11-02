package com.cs201.g1t1.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
public class Category {
    @Id
    private String categoryName;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    private Set<Business> businesses;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
