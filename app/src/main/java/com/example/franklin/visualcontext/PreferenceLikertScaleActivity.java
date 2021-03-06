package com.example.franklin.visualcontext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.franklin.visualcontext.data.restaurant.PreferenceLevels;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


import static com.example.franklin.visualcontext.Constants.JSON_PREFERENCES_KEY;
import static com.example.franklin.visualcontext.Constants.USER_PREFS_FILE_NAME;

/**
 * page where you select a preference option for a certain ingredient/type of food
 */
public class PreferenceLikertScaleActivity extends AppCompatActivity implements View.OnClickListener {

    private static String ingredientName;

    private static PreferenceLevels preferenceLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_page);

        Intent intent = getIntent();
        ingredientName = intent.getStringExtra(Constants.INGREDIENT_NAME_EXTRA_MESSAGE);

        final RadioButton likeStrongly = (RadioButton)findViewById(R.id.likeStrongly);
        final RadioButton like = (RadioButton)findViewById(R.id.like);
        final RadioButton neutral = (RadioButton)findViewById(R.id.neutral);
        final RadioButton dislike = (RadioButton)findViewById(R.id.dislike);
        final RadioButton dislikeStrongly = (RadioButton)findViewById(R.id.dislikeStrongly);
        File file = new File(getApplicationContext().getFilesDir(), Constants.USER_PREFS_FILE_NAME);

        try {
            InputStream is = new FileInputStream(file);
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line;

            while ( (line = rd.readLine()) != null ){
                if(line.matches(".*"+ingredientName+"\":\"LIKE_STRONGLY"+".*")){ //--regex of what to search--
                    likeStrongly.setChecked(true);
                }
                else if(line.matches(".*"+ingredientName+"\":\"LIKE\""+".*")){ //--regex of what to search--
                    like.setChecked(true);
                }
                else if(line.matches(".*"+ingredientName+"\":\"NEUTRAL"+".*")){ //--regex of what to search--
                    neutral.setChecked(true);
                }
                else if(line.matches(".*"+ingredientName+"\":\"DISLIKE\""+".*")){ //--regex of what to search--
                    dislike.setChecked(true);
                }
                else if(line.matches(".*"+ingredientName+"\":\"DISLIKE_STRONGLY"+".*")){ //--regex of what to search--
                    dislikeStrongly.setChecked(true);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Button one = (Button) findViewById(R.id.button9);
        one.setOnClickListener(this);
    }

    /**
     * Submits the preference change
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button9:
                editPreferencesAndSave(ingredientName, preferenceLevel);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * Edits the preferences file to have the updated preference level for the associated ingredient
     */
    private void editPreferencesAndSave(String ingredientName, PreferenceLevels preferenceLevel) {
        File file = new File(getApplicationContext().getFilesDir(), USER_PREFS_FILE_NAME);
        try (FileInputStream in = new FileInputStream(file)) {
            String jsonString = IOUtils.toString(in);
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject prefObject = jsonObject.getJSONObject(JSON_PREFERENCES_KEY);
            prefObject.put(ingredientName, preferenceLevel.toString());
            jsonObject.put(JSON_PREFERENCES_KEY, prefObject);
            FileOutputStream out = new FileOutputStream(file);
            out.write(jsonObject.toString().getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(this.getClass().getSimpleName(), "unexpected JSON exception", e);
        }
    }

    public void onRadioButtonClicked(View view) {
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.likeStrongly:
                preferenceLevel = PreferenceLevels.LIKE_STRONGLY;
                break;
            case R.id.like:
                preferenceLevel = PreferenceLevels.LIKE;
                break;
            case R.id.neutral:
                preferenceLevel = PreferenceLevels.NEUTRAL;
                break;
            case R.id.dislike:
                preferenceLevel = PreferenceLevels.DISLIKE;
                break;
            case R.id.dislikeStrongly:
                preferenceLevel = PreferenceLevels.DISLIKE_STRONGLY;
                break;
        }
    }
}
