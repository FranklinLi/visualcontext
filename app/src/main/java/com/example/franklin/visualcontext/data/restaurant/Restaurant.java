package com.example.franklin.visualcontext.data.restaurant;

import android.support.annotation.Nullable;

import com.example.franklin.visualcontext.data.Place;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Represents a restaurant
 */
public class Restaurant extends Place {

    /**
     * Restaurant menu
     */
    @Setter
    @Getter
    private Menu menu;

    /**
     * Price level of the restaurant
     */
    @Getter
    @NonNull
    private final Integer priceLevel;

    /**
     *  Menu can be null if system hasn't loaded that part yet. Menu can be set
     *  when the user wants to see it.
     */
    public Restaurant(@NonNull final String id, @NonNull final CharSequence name, @Nullable final Menu
            menu, @NonNull final Integer priceLevel) {
        super(id, name);
        this.menu = menu;
        this.priceLevel = priceLevel;
    }

    @Override
    public Object getPlaceDetails() {
        return menu;
    }

    /**
     * Restaurant's place detail type is {@link Menu}
     */
    @Override
    public Class getPlaceDetailType() {
        return menu.getClass();
    }
}
