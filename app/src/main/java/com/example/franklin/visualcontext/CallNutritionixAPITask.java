package com.example.franklin.visualcontext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.franklin.visualcontext.data.nutrition.AggregateNutritionData;
import com.example.franklin.visualcontext.data.nutrition.NutritionixInfoAcquirer;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CallNutritionixAPITask  extends AsyncTask<String,  String, AggregateNutritionData> {

    private Context context;

    public CallNutritionixAPITask(Context context) {
        this.context = context;
    }

    @Override
    protected AggregateNutritionData doInBackground(String... objects) {
        String food = objects[0];
        try {
            return NutritionixInfoAcquirer.getNutritionInfo(food);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("API call failed with food " + food);
    }

    /**
     * what happens when the call is finished
     * @param data
     */
    protected void onPostExecute(AggregateNutritionData data) {
        String calories = data.getNutritionData().get(AggregateNutritionData.NutritionDataKeys.CALORIES).toDisplayString();
        String carbs = data.getNutritionData().get(AggregateNutritionData
                .NutritionDataKeys.CARBS).toDisplayString();
        String fats = data.getNutritionData().get(AggregateNutritionData
                .NutritionDataKeys.FATS).toDisplayString();
        String sodium = data.getNutritionData().get(AggregateNutritionData
                .NutritionDataKeys.SODIUM).toDisplayString();
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String date_json = date + ".json";
            File file = new File(context.getFilesDir(), date_json);
            WriteToReport.WriteToJsonReport(file, calories, carbs, sodium, fats, data.getFoodName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("CallNutritionAPITask", "wrote food " + data.getFoodName() + " to json file");
        createAlertDialog("Your food intake has been successfully saved").show();
    }

    private AlertDialog createAlertDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Add the buttons
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);

            }
        });
        builder.setTitle(title);
        return builder.create();
    }
}
