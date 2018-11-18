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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
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
    private List<String> likelyPlaceNames = new ArrayList<>();

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
        if (likelyPlaceNames.isEmpty()) {
            //use this for demo/testing if no restaurants near
            likelyPlaceNames.add("Popeyes");
        }
        Utils.show_list(this, likelyPlaceNames);
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
     * request check.
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
     * @return: list of establishment names
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
                            PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                            // Set the count, handling cases where less than 5 entries are returned.
                            int count = Math.min(likelyPlaces.getCount(), M_MAX_ENTRIES);
                            List<String> mLikelyPlaceNames = new ArrayList<>();
                            for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                // Build a list of likely places to show the user.
                                mLikelyPlaceNames.add((String) placeLikelihood.getPlace().getName());
                                if (mLikelyPlaceNames.size() == count ) {
                                    break;
                                }
                            }
                            likelyPlaceNames = mLikelyPlaceNames;
                            // Release the place likelihood buffer, to avoid memory leaks.
                            likelyPlaces.release();
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
                    Intent intent = new Intent(this, PreferenceActivity.class);
                    startActivity(intent);
                break;
                case R.id.button2:
                    intent = new Intent(this, RestrictionActivity.class);
                    startActivity(intent);
                // do your code
                break;

            default:
                break;
        }
    }
}
