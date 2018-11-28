package com.example.franklin.visualcontext.data.restaurant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents each diet restriction that the user can set.
 */
@AllArgsConstructor
@Getter
public enum DietRestrictions {

    GLUTEN_FREE(new HashSet<>(Arrays.asList("wheat", "soy sauce", "bread",
            "bagel", "pizza", "pasta", "pancake", "pizza", "rye", "barley"))),

    PEANUT_ALLERGY(new HashSet<>(Arrays.asList("peanut"))),

    VEGETARIAN(new HashSet<>(Arrays.asList("meat", "beef", "chicken", "pork", "fish", "steak",
            "ham", "turkey", "tenderloin", "rib"))),

    KOSHER(new HashSet<>(Arrays.asList("pork", "pig", "horse", "camel", "rabbit", "bacon"))),

    HALAL(new HashSet<>(Arrays.asList("pork", "pig", "reptile", "frog", "beetle", "bug",
            "insect", "shellfish", "shrimp", "scallop", "bacon")));

    /**
     * Ingredients that the diet restriction restricts from
     */
    private Set<String> restrictedIngredients;
}
