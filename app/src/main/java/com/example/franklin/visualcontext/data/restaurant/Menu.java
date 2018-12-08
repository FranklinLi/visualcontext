package com.example.franklin.visualcontext.data.restaurant;

import android.util.Log;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    private final List<MenuItem> menuItems;

    /**
     * Creates a menu listing based on user's preferences, filtering out restrictions and then
     * sorting by preference score
     * @param menu the raw menu with all menu items
     * @param file the user's preferences file
     * @return the sorted and filtered menu
     */
    public static Menu filterAndSortMenuItems(Menu menu, File file) {
        PreferenceMetadata metadata;
        List<DietRestrictions> dietRestrictions = new ArrayList<>();
        List<PreferenceIngredient> preferenceIngredients =Arrays.asList(PreferenceIngredient
                .values());
        try (FileInputStream in = new FileInputStream(file)) {
            metadata = PreferenceMetadata.createFromJson(in);
            dietRestrictions = metadata.getDietRestrictions();
            preferenceIngredients = metadata.getPreferenceIngredients();
        } catch (FileNotFoundException e) {
            Log.d(Menu.class.getSimpleName(), "could not find preferences file, using empty diet " +
                    "restrictions and 0 for all preference scores");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //First filter out any menu items that don't fit at least one diet restriction
        List<MenuItem> rawMenuItems = menu.getMenuItems();
        List<MenuItem> restrictionFilteredMenuItems = new ArrayList<>();
        for (MenuItem menuItem: rawMenuItems) {
            boolean fitsAllRestrictions = true;
            for (DietRestrictions restriction : dietRestrictions) {
                if (!menuItem.fitsDietRestriction(restriction)) {
                    fitsAllRestrictions = false;
                    break;
                }
            }
            if (fitsAllRestrictions) {
                restrictionFilteredMenuItems.add(menuItem);
            }
        }

        List<MenuItem> sortedAndFilteredMenuItems = restrictionFilteredMenuItems;
        //calculates preference scores
        for (MenuItem menuItem : sortedAndFilteredMenuItems) {
            menuItem.updatePreferenceScore(preferenceIngredients);
        }
        //sorts based on preference scores in descending order
        Collections.sort(sortedAndFilteredMenuItems, Collections.<MenuItem>reverseOrder());
        return new Menu(sortedAndFilteredMenuItems);
    }
}
