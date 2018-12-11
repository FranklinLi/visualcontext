package com.example.franklin.visualcontext.data.nutrition;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Object representing types of nutrition data we keep track of
 */
@Getter
@AllArgsConstructor
public abstract class NutritionData {

    /**
     * The numerical value of the nutrition
     */
    private final Double value;

    /**
     * The unit of the nutrition info
     */
    private final NutritionUnitTypes unitType;

    /**
     * Converts to a string to be displayed to the user and saved in the json file
     * @return
     */
    public String toDisplayString() {
        return value + unitType.getUnitAbbreviation();
    }

    public Double toDisplayDouble() {return value;}
    /**
     * Calories data model
     */
    public static class Calories extends NutritionData {
        public Calories(Double value) {
            super(value, NutritionUnitTypes.CALORIES);
        }
    }

    /**
     * Carbs data model
     */
    public static class Carbohydrates extends NutritionData {
        public Carbohydrates(Double value) {
            super(value, NutritionUnitTypes.GRAMS);
        }
    }

    /**
     * Sodium data model
     */
    public static class Sodium extends NutritionData {
        public Sodium(Double value) {
            super(value, NutritionUnitTypes.MILLIGRAMS);
        }
    }

    /**
     * Fats data model
     */
    public static class Fat extends NutritionData {
        public Fat(Double value) {
            super(value, NutritionUnitTypes.GRAMS);
        }
    }
}
