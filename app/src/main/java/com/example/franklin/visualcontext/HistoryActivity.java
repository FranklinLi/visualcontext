package com.example.franklin.visualcontext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.franklin.visualcontext.data.MetadataTranslation;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/*
 This class is used to show the report
 */

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {


    public String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Intent intent = getIntent();
        filename = intent.getStringExtra(Constants.INGREDIENT_NAME_EXTRA_MESSAGE) + ".json";


        TextView history = (TextView) findViewById(R.id.textView);
        String final_output = "";

        File file = new File(getApplicationContext().getFilesDir(), filename);
        Button btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(this);
        try (FileInputStream in = new FileInputStream(file)) {
            String jsonString = IOUtils.toString(in);
            JSONObject jsonObject = new JSONObject(jsonString);
            //each list item has different kinds of nutrition data for each dish
            List<String> displayStrings = MetadataTranslation.jsonToDisplayStrings(jsonObject);
            //Populate listview with all the strings from displayStrings here
            //this is just for showing it, but not using listview right now
            for (String str: displayStrings) {
                final_output += str;
            }
            //this is daily summary for aggregate daily stats, show this at top or bottom of
            // listview
            String daily_summary = MetadataTranslation.jsonToDailyAggregateDisplayString
                    (jsonObject);
            final_output += daily_summary;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        history.setText(final_output);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button2:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }


}

