package com.example.franklin.visualcontext.data;

import com.example.franklin.visualcontext.data.restaurant.Restaurant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceToTypeMapping {

    /**
     * Defines mapping from a {@link Place} subclass to Google's place types
     */
    public static final Map<Class<? extends Place>, List<Integer>> placeToTypes;
    static
    {
        placeToTypes = new HashMap<>();
        placeToTypes.put(Restaurant.class,  Arrays.asList(
            com.google.android.gms.location.places.Place.TYPE_RESTAURANT,
            com.google.android.gms.location.places.Place.TYPE_FOOD,
            com.google.android.gms.location.places.Place.TYPE_CAFE,
            com.google.android.gms.location.places.Place.TYPE_MEAL_DELIVERY,
            com.google.android.gms.location.places.Place.TYPE_MEAL_TAKEAWAY));
    }
}
