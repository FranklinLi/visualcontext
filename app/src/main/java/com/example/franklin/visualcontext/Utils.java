package com.example.franklin.visualcontext;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.franklin.visualcontext.data.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.NonNull;

/**
 * Utilities class
 */
public class Utils {

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
}
