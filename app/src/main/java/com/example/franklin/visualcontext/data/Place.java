package com.example.franklin.visualcontext.data;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a place we show to the user
 */
@AllArgsConstructor
public abstract class Place implements PlaceDetails, Serializable {

    /**
     * Unique identifier
     */
    @Getter
    @NonNull
    private final String id;

    /**
     * name of the place. Displayed on the device.
     */
    @Getter
    @NonNull
    private final CharSequence name;

    /**
     * Returns this place's details to be displayed
     */
    @Override
    public abstract Object getPlaceDetails();
}
