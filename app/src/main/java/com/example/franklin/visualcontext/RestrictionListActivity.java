package com.example.franklin.visualcontext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.franklin.visualcontext.data.restaurant.DietRestrictions;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import static com.example.franklin.visualcontext.Constants.JSON_RESTRICTIONS_KEY;

/**
 * Page where you select all your dietary restrictions
 */
public class RestrictionListActivity extends AppCompatActivity implements View.OnClickListener {

    CheckBox peanutallergies, halal, glutenfree, vegetarian, kosher;
    String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        peanutallergies = findViewById(R.id.PeanutAllergies);
        halal = findViewById(R.id.Halal);
        glutenfree = findViewById(R.id.GlutenFree);
        vegetarian = findViewById(R.id.Vegetarian);
        kosher = findViewById(R.id.Kosher);

        Button one = findViewById(R.id.button7);
        one.setOnClickListener(this);
    }

    private void editRestrictionsAndSave() {
        File file = new File(getFilesDir(), Constants.USER_PREFS_FILE_NAME);
        try (FileInputStream in = new FileInputStream(file)) {
            String jsonStr = IOUtils.toString(in);
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray restrictionsArray = new JSONArray();
            if (peanutallergies.isChecked()) {
                restrictionsArray.put(DietRestrictions.PEANUT_ALLERGY.toString());
            }
            if (halal.isChecked()) {
                restrictionsArray.put(DietRestrictions.HALAL.toString());
            }
            if (kosher.isChecked()) {
                restrictionsArray.put(DietRestrictions.KOSHER.toString());
            }
            if (vegetarian.isChecked()) {
                restrictionsArray.put(DietRestrictions.VEGETARIAN.toString());
            }
            if (glutenfree.isChecked()) {
                restrictionsArray.put(DietRestrictions.GLUTEN_FREE.toString());
            }
            jsonObj.put(JSON_RESTRICTIONS_KEY, restrictionsArray);
            FileOutputStream out = new FileOutputStream(file);
            out.write(jsonObj.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button7:
                editRestrictionsAndSave();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
