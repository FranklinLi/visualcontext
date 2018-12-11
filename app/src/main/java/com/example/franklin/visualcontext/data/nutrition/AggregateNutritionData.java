package com.example.franklin.visualcontext.data.nutrition;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * Represents all the nutrition data for one food item
 */
@Getter
@AllArgsConstructor
public class AggregateNutritionData {
    /**
     * Keys for keying into the nutrient data map
     */
    public enum NutritionDataKeys {CALORIES, CARBS, FATS, SODIUM}

    private final String foodName;

    /**
     * Maps from nutrition type to
     * its data model
     */
    private final Map<NutritionDataKeys, NutritionData> nutritionData;

}
