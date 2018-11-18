package com.example.franklin.visualcontext;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Utilities class
 */
public class Utils {

    /**
     * Populates ListView with a list
     * @return the list view
     */
    public static View show_list(Activity activity, List<String> list) {
        ArrayAdapter<String> data_adapter = new ArrayAdapter<>(activity,R.layout.list_item, list);
        ListView data_view = activity.findViewById(R.id.list_view);
        data_view.setAdapter(data_adapter);
        return data_view;
    }
}
