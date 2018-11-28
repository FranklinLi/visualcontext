package com.example.franklin.visualcontext.data.restaurant;

import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a menu item for a restaurant
 */
public class MenuItem implements Comparable<MenuItem>{

    /**
     * Collection of ingredients
     */
    @Getter
    private final Collection<String> ingredients;

    /**
     * price of the item in CAD
     */
    @Getter
    private final Double price;

    /**
     * name of the menu item
     */
    @Getter
    @NonNull
    private final String name;

    /**
     * Represents the score given to this menu item. The higher it is, the more preferred this
     * item is. Used for ranking menu items to display to the user. Default value is 0.
     */
    @Getter
    private Integer preferenceScore = 0;

    public MenuItem(@NonNull final Collection<String> ingredients, @NonNull final Double price,
                    @NonNull final String name) {
        this.ingredients = ingredients;
        this.price = price;
        this.name = name;
    }

    /**
     * Searches for a list of user's preferred ingredients in this menu item's list of ingredients,
     * and updates the preference score accordingly
     */
    public void updatePreferenceScore(@NonNull final List<PreferenceIngredient>
                                              preferenceIngredients) {

        for (PreferenceIngredient preferenceIngredient : preferenceIngredients) {
            for (String ingredient : ingredients) {
                if (ingredient.toLowerCase().contains(preferenceIngredient.toString().toLowerCase())) {
                    preferenceScore += preferenceIngredient.getPrefLevel().getScore();
                }
            }
        }
    }

    /**
     * Checks whether this menu item matches a diet restriction based on its ingredients
     */
    public boolean fitsDietRestriction(@NonNull final DietRestrictions restriction) {
        for (String ingredient : ingredients) {
            for (String restrictedIngredient : restriction.getRestrictedIngredients()) {
                if (ingredient.toLowerCase().contains(restrictedIngredient.toLowerCase()) ||
                        restrictedIngredient.toLowerCase().contains(ingredient)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * For sorting based on preference score
     */
    @Override
    public int compareTo(MenuItem o) {
        return (preferenceScore - o.preferenceScore);
    }
}
