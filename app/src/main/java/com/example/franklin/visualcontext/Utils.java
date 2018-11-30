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
import com.example.franklin.visualcontext.data.restaurant.PreferenceIngredient;
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

     //JSON menu reading methods

    /**
     * Reads a Menu Object from the relevant file
     */
    public static Menu readMenuFromJSON(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                //skip to to the menu item list
                String key = reader.nextName();
                if (key.equals("menu")) {
                    return new Menu(readMenuItemsFromJSON(reader));
                } else {
                    reader.skipValue();
                }
            }
        } finally {
            reader.close();
        }
        throw new IllegalStateException("Menu not found or improperly formatted in file");
    }

    /**
     * Reads a list of menu items from JSON
     */
    public static List<MenuItem> readMenuItemsFromJSON(JsonReader reader) throws IOException {
        List<MenuItem> menuItems = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            menuItems.add(readMenuItemFromJSON(reader));
        }
        reader.endArray();
        return menuItems;
    }

    /**
     * Reads a {@link MenuItem} from JSON
     */
    public static MenuItem readMenuItemFromJSON(JsonReader reader) throws IOException {
        Set<String> ingredients = new HashSet<>();
        Double price = null;
        String name = null;
        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if (key.equals("name")) {
                name = reader.nextString();
            } else if (key.equals("price")) {
                price = reader.nextDouble();
            } else if (key.equals("ingredients") && reader.peek() != JsonToken.NULL) {
                ingredients = readIngredientsFromJSON(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new MenuItem(ingredients, price, name);
    }

    /**
     * Reads a set of ingredients from JSON
     */
    public static Set<String> readIngredientsFromJSON(JsonReader reader) throws IOException {
        Set<String> ingredients = new HashSet<>();
        reader.beginArray();
        while (reader.hasNext()) {
            ingredients.add(reader.nextString());
        }
        reader.endArray();
        return ingredients;
    }
}
