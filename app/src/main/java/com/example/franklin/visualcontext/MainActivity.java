package com.example.franklin.visualcontext;

import android.Manifest;
import android.annotation.SuppressLint;
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
import com.example.franklin.visualcontext.data.restaurant.Menu;
import com.example.franklin.visualcontext.data.restaurant.Restaurant;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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


    File root = android.os.Environment.getExternalStorageDirectory();
    File dir = new File (root.getAbsolutePath());
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button one = findViewById(R.id.button2);
        one.setOnClickListener(this); // calling onClick() method
        Button two = findViewById(R.id.button3);
        two.setOnClickListener(this);

        mGeoDataClient = Places.getGeoDataClient(this);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);
        checkPermissionsAndGetLikelyPlaceNames();
        if (likelyPlaces.isEmpty()) {
            //use this for demo/testing if no restaurants near
            likelyPlaces.add(new Restaurant("abc", "Popeyes' Chicken", null, 2));
            likelyPlaces.add(new Restaurant("bcd", "Burger King", null, 2));
        }
        View placesView = Utils.show_place_names_list(this, likelyPlaces);
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
     * Asks permissions for location if app does not have fine location permission. Otherwise,
     * calls the Google Places API
     */
    private void checkPermissionsAndGetLikelyPlaceNames() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //permission not already granted
            requestPermissions( new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
        } else {
            callPlaceDetectionApi();
        }
    }

    /**
     * Call back after {@code requestPermissions} is called. Checks for the results of permission
     * request.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)   {
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
        //only look for places open now
        placeFilter.isRestrictedToPlacesOpenNow();
        @SuppressLint("MissingPermission") Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(placeFilter);
        placeResult.addOnCompleteListener
                (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            PlaceLikelihoodBufferResponse likelyPlaceResults = task.getResult();

                            // Set the count, handling cases where less than 5 entries are returned.
                            int count = Math.min(likelyPlaceResults.getCount(), M_MAX_ENTRIES);
                            for (PlaceLikelihood placeLikelihood : likelyPlaceResults) {
                                List placeTypes = placeLikelihood.getPlace().getPlaceTypes();
                                //for now, just focusing on restaurants, generify this part if we
                                // move on to other kinds of places.
                                placeTypes.retainAll(PlaceToTypeMapping.placeToTypes.get(Restaurant.class));
                                if (placeTypes.isEmpty()) {
                                    return;
                                }
                                com.google.android.gms.location.places.Place googlePlace =
                                        placeLikelihood.getPlace();
                                //menu null for now, will load when loading into DetailActivity
                                Place place = new Restaurant(googlePlace.getId(), googlePlace
                                        .getName(), null, googlePlace.getPriceLevel());
                                // Build a list of likely places to show the user.
                                likelyPlaces.add(place);
                                if (likelyPlaces.size() == count ) {
                                    break;
                                }
                            }
                            // Release the place likelihood buffer, to avoid memory leaks.
                            likelyPlaceResults.release();
                        } else {
                            Log.e(TAG, "Exception: %s", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {



                case R.id.button3:
                    file = new File(dir, "preference.json");
                    JSONObject jsonObj = new JSONObject();
                    try {

                        if (file.length()==0){
                            FileOutputStream f = new FileOutputStream(file);
                            PrintWriter pw = new PrintWriter(f);
                            jsonObj.put("pork","neutral");
                            jsonObj.put("chicken","neutral");
                            jsonObj.put("beef","neutral");
                            jsonObj.put("seafood","neutral");
                            jsonObj.put("peanut allergies", "no");
                            jsonObj.put("halal", "no");
                            jsonObj.put("kosher", "no");
                            jsonObj.put("vegetarian", "no");
                            jsonObj.put("glutenfree", "no");
                            pw.println(jsonObj.toString());
                            pw.flush();
                            pw.close();
                            f.close();
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (JSONException e) {
                        Log.e("MYAPP", "unexpected JSON exception", e);
                        // Do something to recover ... or kill the app.
                    }
                    Intent intent = new Intent(this, PreferenceActivity.class);
                    startActivity(intent);
                break;
                case R.id.button2:
                    file = new File(dir, "preference.json");
                    JSONObject jsonObj1 = new JSONObject();
                    try {

                        if (file.length()==0){
                            FileOutputStream f = new FileOutputStream(file);
                            PrintWriter pw = new PrintWriter(f);
                            jsonObj1.put("pork","neutral");
                            jsonObj1.put("chicken","neutral");
                            jsonObj1.put("beef","neutral");
                            jsonObj1.put("seafood","neutral");
                            jsonObj1.put("peanut allergies", "no");
                            jsonObj1.put("halal", "no");
                            jsonObj1.put("kosher", "no");
                            jsonObj1.put("vegetarian", "no");
                            jsonObj1.put("glutenfree", "no");
                            pw.println(jsonObj1.toString());
                            pw.flush();
                            pw.close();
                            f.close();
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (JSONException e) {
                        Log.e("MYAPP", "unexpected JSON exception", e);
                        // Do something to recover ... or kill the app.
                    }
                    intent = new Intent(this, RestrictionActivity.class);
                    startActivity(intent);
                // do your code
                break;

            default:
                break;
        }
    }
}
