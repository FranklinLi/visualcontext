package com.example.franklin.visualcontext.data.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static com.example.franklin.visualcontext.data.restaurant.PreferenceLevels.NEUTRAL;

/**
 * Represents an ingredient that the user can set a preference level for
 */
@AllArgsConstructor
@Getter
public enum PreferenceIngredient {

    BEEF(NEUTRAL),

    SEAFOOD(NEUTRAL),

    CHICKEN(NEUTRAL),

    CRUCIFEROUS(NEUTRAL),

    MARROW(NEUTRAL),

    ROOT(NEUTRAL),

    PORK(NEUTRAL);


    /**
     * The preference level for the ingredient. Default is neutral
     */
    @Setter
    private PreferenceLevels prefLevel;
}
