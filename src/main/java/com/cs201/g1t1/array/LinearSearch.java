package com.cs201.g1t1.array;

import java.util.*;
import com.cs201.g1t1.model.Business;
import com.cs201.g1t1.model.Category;

import org.springframework.stereotype.Component;


@Component
public class LinearSearch {

     /*
    • Write a method named findMostPopularBusiness that returns the business with the most number of occurences in the array
    • Write a method named findOccurences that returns the number of occurences of a particular business  
    the array
    */

    //Resizes internal array to have given capacity >= size.

    protected void resize(int capacity, int [] input) {
        int [] temp = new int [capacity];
        for (int i = 0; i < input.length; i++){
            temp[i] = input[i];
        }
        input = temp; // start using the new array
    }

    
    public int findOccurences (String categoryName, List <Business> businesses){

        int occurences = 0;

        List <Category> categories;

        for (int i = 0; i < businesses.size(); i++){
             categories = new ArrayList <Category> (businesses.get(i).getCategories());
            for (int j = 0; j < categories.size(); j++){
                if (categories.get(j).getCategoryName().equals(categoryName)){
                    occurences++;
                }
            }
        }
        return occurences;
    }

    // public int findOccurences (Category category, List <Business> businesses){

    //     int occurences = 0;

    //     for (int i = 0; i < businesses.size(); i++){
    //         if (businesses.get(i).getCategories().contains(category)){
    //             occurences++;
    //         }
    //     }
    //     return occurences;
    // }

    public int findMax (List <Integer> input) {
        int max = input.get(0);
        int index = 0;
        for (int i = 1; i < input.size();i++){
            if (input.get(i) > max){
                max = input.get(i);
                index = i;
            }
        }
        return index;
    }

    // public Category findMostPopularCategory(List <Business> businesses, int size){

    //     List <Category> categories = new ArrayList <Category> ();
    //     int [] categoriesOcc = new int [size]; 

    //     for (int i = 0; i < businesses.size(); i++){
    //         for (Category c: businesses.get(i).getCategories()){
    //             if (!categories.contains(c)){
    //                 if (categories.size() == size-2){
    //                     resize(size * 2, categoriesOcc);
    //                     size *= 2;
    //                 }
    //                 categories.add(c);
    //             }
    //             categoriesOcc[categories.indexOf(c)]++;
    //         }
    //     }
    //     int highestCountIndex = findMax(categoriesOcc);
    //     Category mostPopular = categories.get(highestCountIndex);

    //     return mostPopular;

    // }


    public Category findMostPopularCategory(List <Business> businesses){

        List <Category> categories = new ArrayList <Category> ();
        List <Integer> categoriesOcc = new ArrayList <Integer>(); 

        for (int i = 0; i < businesses.size(); i++){
            for (Category c: businesses.get(i).getCategories()){
                if (!categories.contains(c)){
                    categories.add(c);
                    categoriesOcc.add(categories.indexOf(c),1);
                } else {
                    categoriesOcc.add(categories.indexOf(c), categoriesOcc.get(categories.indexOf(c)) + 1);
                }
                
            }
        }
        int highestCountIndex = findMax(categoriesOcc);
        Category mostPopular = categories.get(highestCountIndex);

        return mostPopular;

    }
    
}
