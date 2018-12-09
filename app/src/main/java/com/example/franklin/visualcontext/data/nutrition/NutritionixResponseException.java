package com.example.franklin.visualcontext.data.nutrition;

import lombok.Getter;
import lombok.NonNull;

/**
 * Exception to be thrown when call to nutritionix leads to an error (response code other than 200)
 */
public class NutritionixResponseException extends Exception {

    @Getter
    private Integer responseCode;

    public NutritionixResponseException(@NonNull final Integer responseCode, @NonNull final String
                                        message) {
        super("Call to Nutritionix failed with response code " + responseCode + " and error " +
                "message " + message);
        this.responseCode = responseCode;
    }
}
