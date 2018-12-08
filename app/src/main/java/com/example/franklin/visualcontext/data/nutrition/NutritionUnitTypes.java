package com.example.franklin.visualcontext.data.nutrition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * Defines the types of nutrition unit
 */
@AllArgsConstructor
public enum NutritionUnitTypes {

    CALORIES("cal"),

    MILLIGRAMS("mg"),

    GRAMS("g");

    @NonNull
    @Getter
    private final String unitAbbreviation;
}
