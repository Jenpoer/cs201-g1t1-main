package com.cs201.g1t1.List;

import java.util.*;
import com.cs201.g1t1.model.Business;
import com.cs201.g1t1.model.Category;

import org.springframework.stereotype.Component;

@Component
public class LinearSearch {

    /**
     * Find the most popular category in input list of businesses
     * @param businesses list of businesses to be searched
     * @return the name of the most popular category
     */
    public Category findMostPopularCategory(List <Business> businesses){
        List <Category> categories = new ArrayList <Category> ();
        List <Integer> categoriesOcc = new ArrayList <Integer>(); 

        for (int i = 0; i < businesses.size(); i++){
            // Loop through categories of the business as businesses can have more than 1 category 
            for (Category c: businesses.get(i).getCategories()){ 
                if (!categories.contains(c)){
                    categories.add(c);
                    categoriesOcc.add(categories.indexOf(c),1);
                } else {
                    categoriesOcc.set(categories.indexOf(c), categoriesOcc.get(categories.indexOf(c)) + 1);
                }
            }
        }

        int highestCountIndex = findMax(categoriesOcc);
        Category mostPopular = categories.get(highestCountIndex);

        return mostPopular;
    }

    /**
     * Counts the number of businesses in the input list that are of a particular category 
     * @param categoryName category to count the number of occurances of
     * @param businesses list of businesses to search
     * @return the number of businesses in the list that are of the category categoryName
     */
    public int findOccurrences (String categoryName, List <Business> businesses){
        int occurrences = 0;

        List <Category> categories;

        for (int i = 0; i < businesses.size(); i++){
             categories = new ArrayList <Category> (businesses.get(i).getCategories());
            for (int j = 0; j < categories.size(); j++){
                if (categories.get(j).getCategoryName().equals(categoryName)){
                    occurrences++;
                }
            }
        }
        return occurrences;
    }

    /**
     * Find the maximum number in the input list and return it
     * @param input list of numbers to be searched
     * @return the largest number in the list
     */
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

}
