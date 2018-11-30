package com.example.franklin.visualcontext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.franklin.visualcontext.data.Place;
import com.example.franklin.visualcontext.data.PlaceToTypeMapping;
import com.example.franklin.visualcontext.data.restaurant.PreferenceIngredient;
import com.example.franklin.visualcontext.data.restaurant.PreferenceLevels;
import com.example.franklin.visualcontext.data.restaurant.Restaurant;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static com.example.franklin.visualcontext.Constants.JSON_PREFERENCES_KEY;
import static com.example.franklin.visualcontext.Constants.JSON_RESTRICTIONS_KEY;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {


    /**
     * For getting data about a place
     */
    protected GeoDataClient mGeoDataClient;

    /**
     * For calling current place api
     */
    protected PlaceDetectionClient mPlaceDetectionClient;

    /**
     * Maximum number of restaurants to be shown
     */
    private static final int M_MAX_ENTRIES = 10;

    /**
     * for logging
     */
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Request permissions code for fine location
     */
    private static final int LOCATION_REQUEST = 1;

    /**
     * Stores list of places that user is currently around.
     */
    private List<Place> likelyPlaces = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button one = findViewById(R.id.preferences);
        one.setOnClickListener(this); // calling onClick() method
        Button two = findViewById(R.id.restrictions);
        two.setOnClickListener(this);

        mGeoDataClient = Places.getGeoDataClient(this);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);
        checkPermissionsAndGetLikelyPlaceNames();
        //this one is for demoing purposes for a menu we hardcoded
        likelyPlaces.add(new Restaurant("abc", "PERFECT Chinese Restaurant", null, 2));
        displayPlaces();
    }

    /**
     * Asks permissions for location if app does not have fine location permission. Otherwise,
     * calls the Google Places API
     */
    private void checkPermissionsAndGetLikelyPlaceNames() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //permission not already granted
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
        } else {
            callPlaceDetectionApi();
        }
    }

    /**
     * Call back after {@code requestPermissions} is called. Checks for the results of permission
     * request.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    callPlaceDetectionApi();
                } else {
                    //permission was denied
                    Toast.makeText(this, "Location permission denied, unable to find nearby " +
                            "establishments", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    /**
     * Calls API to find list of establishments close to current place.
     */
    private void callPlaceDetectionApi() {
        PlaceFilter placeFilter = new PlaceFilter();
        @SuppressLint("MissingPermission") final Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(placeFilter);
        placeResult.addOnCompleteListener
                (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Log.d(TAG, "Google API getCurrentPlace call successful");
                            PlaceLikelihoodBufferResponse likelyPlaceResults = task.getResult();

                            // Set the count, handling cases where less than max entries are returned.
                            int count = Math.min(likelyPlaceResults.getCount(), M_MAX_ENTRIES);
                            for (PlaceLikelihood placeLikelihood : likelyPlaceResults) {
                                Log.d(TAG, "found place " + placeLikelihood.getPlace().getName() + " with likelihood " + placeLikelihood.getLikelihood());

                                List placeTypes = placeLikelihood.getPlace().getPlaceTypes();
                                //for now, just focusing on restaurants, generify this part if we
                                // move on to other kinds of places.
                                placeTypes.retainAll(PlaceToTypeMapping.placeToTypes.get(Restaurant.class));
                                if (placeTypes.isEmpty()) {
                                    continue;
                                }
                                Log.d(TAG, "place " + placeLikelihood.getPlace().getName() + " will be displayed");
                                com.google.android.gms.location.places.Place googlePlace =
                                        placeLikelihood.getPlace();
                                //menu null for now, will load when loading into DetailActivity
                                Place place = new Restaurant(googlePlace.getId(), googlePlace
                                        .getName(), null, googlePlace.getPriceLevel());
                                // Build a list of likely places to show the user.
                                likelyPlaces.add(place);
                                if (likelyPlaces.size() == count) {
                                    break;
                                }
                            }
                            // Release the place likelihood buffer, to avoid memory leaks.
                            likelyPlaceResults.release();
                            Log.d(TAG, "api call complete");
                            displayPlaces();
                        } else {
                            Log.e(TAG, "Exception: %s", task.getException());
                        }
                    }
                });
    }

    private void displayPlaces() {
        //displays list
        View placesView = Utils.show_place_names_list(MainActivity.this,
                likelyPlaces);
        ((ListView) placesView).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Place", likelyPlaces.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    /**
     * Writes to a preferences file that stores user restrictions and preferences if it does not
     * already exist
     */
    private void createPreferencesFileIfEmpty() {
        File file = new File(getApplicationContext().getFilesDir(), Constants.USER_PREFS_FILE_NAME);
        if (file.length() == 0) {
            try (FileOutputStream out = new FileOutputStream(file)) {
                JSONObject jsonObj = new JSONObject();
                JSONObject prefObject = new JSONObject();
                JSONArray restrictionArray = new JSONArray();
                //populate preferences with default preference levels
                for (PreferenceIngredient ingredient : PreferenceIngredient.values()) {
                    prefObject.put(ingredient.toString(), ingredient.getPrefLevel().toString());
                }
                jsonObj.put(JSON_PREFERENCES_KEY, prefObject);
                jsonObj.put(JSON_RESTRICTIONS_KEY, restrictionArray);
                out.write(jsonObj.toString().getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e("MYAPP", "unexpected JSON exception", e);
            }
        }
    }

    /**
     * Handles clicking of buttons on main activity
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.restrictions:
                createPreferencesFileIfEmpty();
                Intent intent = new Intent(this, RestrictionListActivity.class);
                startActivity(intent);
                break;
            case R.id.preferences:
                createPreferencesFileIfEmpty();
                intent = new Intent(this, PreferenceListActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
