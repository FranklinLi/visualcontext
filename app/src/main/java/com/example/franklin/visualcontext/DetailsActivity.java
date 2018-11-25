package com.example.franklin.visualcontext;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.franklin.visualcontext.data.Place;
import com.example.franklin.visualcontext.data.restaurant.Menu;
import com.example.franklin.visualcontext.data.restaurant.Restaurant;

import java.io.IOException;
import java.io.InputStream;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = this.getIntent();
        Place place = (Place) (intent.getExtras().getSerializable("Place"));
        if (place instanceof Restaurant) {
            Menu menu = null;
            try (InputStream in = getResources().openRawResource(getResources().getIdentifier(place.getId(), "raw", getPackageName()))) {
                menu = Utils.readMenuFromJSON(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((Restaurant) place).setMenu(menu);
        }
        Utils.show_place_details(this, place);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



}
