package com.example.franklin.visualcontext.data.restaurant;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents the menu of a restaurant
 */
@AllArgsConstructor
public class Menu {

    /**
     * Collection of menu items
     */
    @NonNull
    @Getter
    private final Collection<MenuItem> menuItems;
}
