package com.example.franklin.visualcontext.data.restaurant;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a menu item for a restaurant
 */
@AllArgsConstructor
public class MenuItem {

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
    private final String name;
}
