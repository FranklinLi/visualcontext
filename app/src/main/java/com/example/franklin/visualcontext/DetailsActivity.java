package com.example.franklin.visualcontext;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.franklin.visualcontext.data.Place;
import com.example.franklin.visualcontext.data.restaurant.Menu;
import com.example.franklin.visualcontext.data.restaurant.MenuItem;
import com.example.franklin.visualcontext.data.restaurant.PreferenceMetadata;
import com.example.franklin.visualcontext.data.restaurant.Restaurant;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import static com.example.franklin.visualcontext.Constants.USER_PREFS_FILE_NAME;

/**
 * Activity for showing a place's details
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = this.getIntent();
        Place place = (Place) (intent.getExtras().getSerializable("Place"));
        boolean detailsFound = true;
        if (place instanceof Restaurant) {
            Menu menu = null;
            try (InputStream in = getResources().openRawResource(getResources().getIdentifier
                    (place.getId(), "raw", getPackageName()))) {
                menu = Utils.readMenuFromJSON(in);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Resources.NotFoundException ex) {
                detailsFound = false;
                Toast.makeText(this, "No menu found for this restaurant", Toast.LENGTH_LONG).show();
            }
            ((Restaurant) place).setMenu(Menu.filterAndSortMenuItems(menu, new File(getFilesDir(),
                    USER_PREFS_FILE_NAME)));

        }
        if (detailsFound) {
            Utils.show_place_details(this, place);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
