package com.example.franklin.visualcontext;

import android.app.Activity;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.franklin.visualcontext.data.Place;
import com.example.franklin.visualcontext.data.restaurant.Menu;
import com.example.franklin.visualcontext.data.restaurant.MenuItem;
import com.example.franklin.visualcontext.data.restaurant.Restaurant;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.NonNull;

/**
 * Utilities class
 */
public class Utils {

    //View showing methods

    /**
     * Populates ListView with a list
     * @return the list view
     */
    public static View show_list(@NonNull final Activity activity, @NonNull final List<CharSequence> list) {
        ArrayAdapter<CharSequence> data_adapter = new ArrayAdapter<>(activity,R.layout.list_item, list);
        ListView data_view = activity.findViewById(R.id.list_view);
        data_view.setAdapter(data_adapter);
        return data_view;
    }

    /**
     * shows a place's names in a ListView
     */
    public static View show_place_names_list(Activity activity, List<Place> places) {
        List<CharSequence> names = new ArrayList<>();
        for (Place place: places) {
            names.add(place.getName());
        }
        return show_list(activity, names);
    }

    /**
     * Shows a place's details in a view based on the type of the place
     */
    public static View show_place_details(Activity activity, Place place) {
        if (place instanceof Restaurant) {
           return show_place_menu_items_list(activity, (Menu) place.getPlaceDetails());
        }
        throw new IllegalArgumentException("Cannot recognize place details for place type " + place
                .getClass().getSimpleName());
    }

    /**
     * shows the menu item names in a list view
     */
    public static View show_place_menu_items_list(Activity activity, Menu menu) {
        List<CharSequence> menuItemNames = new ArrayList<>();
        for (MenuItem item: menu.getMenuItems()) {
            menuItemNames.add(item.getName() + " - " + item.getPrice() + " CAD");
        }
        return show_list(activity, menuItemNames);
    }
}
