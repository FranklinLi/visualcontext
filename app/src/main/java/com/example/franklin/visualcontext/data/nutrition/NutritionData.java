package com.example.franklin.visualcontext.data.nutrition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Object representing types of nutrition data we keep track of
 */
@Getter
@AllArgsConstructor
public abstract class NutritionData {

    /**
     * Name of the nutrition to be displayed
     */
    @NonNull
    private final String name;

    /**
     * The numerical value of the nutrition
     */
    @NonNull
    private Double value;

    /**
     * The unit of the nutrition info
     */
    @NonNull
    private final NutritionUnitTypes unitType;

    /**
     * currently based on a 2000 calories diet
     */
    @NonNull
    private final Double dailyRecommendedValue;

    /**
     * Used for aggregation of daily values
     */
    public void incrementValue(Double value) {
        this.value += value;
    }

    /**
     * Calculates the percentage of the recommended daily value of this nutritional intake
     */
    public Long getPercentageDailyValue() {
        return Math.round((value / dailyRecommendedValue) * 100);
    }

    /**
     * String to be displayed and read to the user
     * @return
     */
    public String toDisplayString() {
        return name + " content of " + value + unitType.getUnitAbbreviation() + " or "
                + getPercentageDailyValue() + "% " +
                "recommended daily value\n";
    }
    /**
     * Calories data model
     */
    public static class Calories extends NutritionData {
        public Calories(Double value) {
            super("calories", value, NutritionUnitTypes.CALORIES, 2000.0);
        }
    }

    /**
     * Carbs data model
     */
    public static class Carbohydrates extends NutritionData {
        public Carbohydrates(Double value) {
            super("carbohydrates", value, NutritionUnitTypes.GRAMS, 325.0);
        }
    }

    /**
     * Sodium data model
     */
    public static class Sodium extends NutritionData {
        public Sodium(Double value) {
            super("sodium", value, NutritionUnitTypes.MILLIGRAMS, 2300.0);
        }
    }

    /**
     * Fats data model
     */
    public static class Fat extends NutritionData {
        public Fat(Double value) {
            super("fat", value, NutritionUnitTypes.GRAMS, 77.0);
        }
    }
}
