package com.example.franklin.visualcontext;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.franklin.visualcontext.data.nutrition.AggregateNutritionData;
import com.example.franklin.visualcontext.data.nutrition.NutritionixInfoAcquirer;
import com.example.franklin.visualcontext.data.nutrition.NutritionixResponseException;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CallNutritionixAPITask  extends AsyncTask<String,  String, AggregateNutritionData> {

    /**
     * Activity this was called from
     */
    private Activity activity;

    public CallNutritionixAPITask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected AggregateNutritionData doInBackground(String... objects) {
        String food = objects[0];
        try {
            return NutritionixInfoAcquirer.getNutritionInfo(food);
        } catch (final Exception e) {
            if (e instanceof NutritionixResponseException) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (((NutritionixResponseException) e).getResponseCode() == 404) {
                            createAlertDialog("Your food could not be found in the database").show();
                        } else {
                            createAlertDialog("Call to Nutritionix failed with error code " +
                                     ((NutritionixResponseException) e).getResponseCode())
                                    .show();
                        }
                    }
                });
            } else {
                createAlertDialog("Operation failed due to an internal error").show();
            }
            //cancelling means onPostExecute will never run
            this.cancel(true);
        }
        throw new IllegalStateException("Async task for Nutritionix API call ran into an " +
                "issue");
    }

    /**
     * what happens when the call is finished
     * @param data
     */
    protected void onPostExecute(AggregateNutritionData data) {
        double calories = data.getNutritionData().get(AggregateNutritionData.NutritionDataKeys
                .CALORIES).getValue();
        double carbs = data.getNutritionData().get(AggregateNutritionData
                .NutritionDataKeys.CARBS).getValue();
        double fats = data.getNutritionData().get(AggregateNutritionData
                .NutritionDataKeys.FATS).getValue();
        double sodium = data.getNutritionData().get(AggregateNutritionData
                .NutritionDataKeys.SODIUM).getValue();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String date_json = date + ".json";
        File file = new File(activity.getFilesDir(), date_json);
        WriteToReport.WriteToJsonReport(file, calories, carbs, sodium, fats, data.getFoodName());

        Log.i("CallNutritionAPITask", "wrote food " + data.getFoodName() + " to json file");
        createAlertDialog("Your food intake has been successfully recorded").show();
    }

    private AlertDialog createAlertDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // Add the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
            }
        });
        builder.setNeutralButton("Report another food", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setTitle(title);
        return builder.create();
    }
}
