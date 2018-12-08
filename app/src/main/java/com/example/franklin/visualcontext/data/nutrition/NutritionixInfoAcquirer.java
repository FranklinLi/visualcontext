package com.example.franklin.visualcontext.data.nutrition;

import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import lombok.NonNull;

/**
 * Responsible for calling Nutrionix API to get information on a dish
 */
public class NutritionixInfoAcquirer {

    /**
     * The application ID for nutrionix API
     */
    private static final String APP_ID = "56d178cd";

    /**
     * The application key for nutrionix API
     */
    private static final String APP_KEY = "047196873d6397c234f3813dabb0ed1a";

    private static final String APP_ID_HEADER = "x-app-id";

    private static final String APP_KEY_HEADER = "x-app-key";

    //this is 0 for dev purposes
    private static final String REMOTE_USER_ID = "0";

    private static final String REMOTE_USER_ID_KEY="x-remote-user-id";

    private static final String TAG = NutritionixInfoAcquirer.class.getSimpleName();

    public static AggregateNutritionData getNutritionInfo(@NonNull final String foodName) throws
            JSONException, IOException {
        URL url = new URL("https://trackapi.nutritionix.com/v2/natural/nutrients");
        InputStream in = null;
        OutputStreamWriter out = null;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestProperty(APP_ID_HEADER, APP_ID);
            urlConnection.setRequestProperty(APP_KEY_HEADER, APP_KEY);
            urlConnection.setRequestProperty(REMOTE_USER_ID_KEY, REMOTE_USER_ID);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");


            JSONObject query = new JSONObject();
            query.put("query", foodName);
            //this combines all foods in the query into one food
            query.put("aggregate", foodName);

            OutputStream outputStream = urlConnection.getOutputStream();
            out = new OutputStreamWriter(outputStream, "UTF-8");
            Log.d(TAG, query.toString());
            out.write(query.toString());
            out.close();
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                in = new BufferedInputStream(urlConnection.getInputStream());
                String response = IOUtils.toString(in);
                JSONObject jsonResponse = new JSONObject(response);
                return extractNutritionDataFromJSON(jsonResponse);
            } else {
                Log.d(TAG, urlConnection.getResponseMessage());
                throw new IllegalStateException("call to nutritionix failed with code " +
                        urlConnection.getResponseCode() + " and error message " +
                        IOUtils.toString(urlConnection
                                .getErrorStream()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            in.close();
            out.close();
            urlConnection.disconnect();
        }
    }

    /**
     * see response structure at https://gist.github.com/mattsilv/95f94dd1378d4747fb68ebb2d042a4a6
     */
    private static AggregateNutritionData extractNutritionDataFromJSON(@NonNull final JSONObject
                                                                             jsonResponse) throws JSONException {
        Map<AggregateNutritionData.NutritionDataKeys, NutritionData> nutritionDataList = new HashMap<>();
        Log.d(TAG, "Json response from nutritionix is " +jsonResponse.toString());
        //should only be one food if we use "aggregate", so just get 0th index
        JSONObject data = jsonResponse.getJSONArray("foods").getJSONObject(0);
        String foodName = data.getString("food_name");
        nutritionDataList.put(AggregateNutritionData.NutritionDataKeys.CALORIES, new NutritionData.Calories((double) data
                .getInt("nf_calories")));
        nutritionDataList.put(AggregateNutritionData.NutritionDataKeys.CARBS, new NutritionData.Carbohydrates((double)
                data
                .getInt("nf_total_carbohydrate")));
        nutritionDataList.put(AggregateNutritionData.NutritionDataKeys.FATS, new NutritionData
                .Fat((double) data
                .getInt("nf_total_fat")));
        nutritionDataList.put(AggregateNutritionData.NutritionDataKeys.SODIUM, new
                NutritionData.Sodium((double) data
                .getInt("nf_sodium")));

        return new AggregateNutritionData(foodName, nutritionDataList);
    }
}
