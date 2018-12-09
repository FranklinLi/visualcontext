package com.example.franklin.visualcontext;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.franklin.visualcontext.data.Place;
import com.example.franklin.visualcontext.data.restaurant.Menu;
import com.example.franklin.visualcontext.data.restaurant.Restaurant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
                menu = Menu.filterAndSortMenuItems(menu,  new File(getFilesDir(),
                        USER_PREFS_FILE_NAME));
            } catch (IOException e) {
                detailsFound = false;
                e.printStackTrace();
            } catch (Resources.NotFoundException ex) {
                detailsFound = false;
                Toast.makeText(this, "No menu found for this restaurant", Toast.LENGTH_LONG).show();
            }

            ((Restaurant) place).setMenu(menu);
        }
        if (detailsFound) {
            final ListView display = (ListView) Utils.show_place_details(this, place);
            //if restaurant, report to nutritionix on click
            if (place instanceof Restaurant) {
                display.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {
                        String foodName = (String) display.getItemAtPosition(position);
                        createConfirmationAlertDialog(foodName).show();
                    }
                });
            }
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private AlertDialog createConfirmationAlertDialog(final String foodName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new CallNutritionixAPITask(DetailsActivity.this).execute(foodName);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setTitle("Are you sure you want to record this dish in your nutrition data?");
        return builder.create();
    }
}
