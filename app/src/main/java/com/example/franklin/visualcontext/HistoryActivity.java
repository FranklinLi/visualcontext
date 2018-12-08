package com.example.franklin.visualcontext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

/*
 This class is used to show the report
 */

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        TextView history = (TextView)findViewById(R.id.textView);
        String final_output="";
        File filesDir = getApplicationContext().getFilesDir();
        File[] files = filesDir.listFiles();
        Button btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(this);
        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        for (File file : files){
            if (file.getName().contains("20")){
                try (FileInputStream in = new FileInputStream(file)) {
                  String jsonString = IOUtils.toString(in);
                  JSONObject jsonObject = new JSONObject(jsonString);
                  JSONArray jsonArray = jsonObject.optJSONArray("dishes");
                  String date = file.getName().replace(".json","");
                  final_output = final_output + date +  "\n";


                    String food;
                    int calorie, carb, sodium;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject row = jsonArray.getJSONObject(i);
                        food = row.getString("food");
                        calorie = row.getInt("calorie");
                        carb = row.getInt("carb");
                        sodium = row.getInt("sodium");
                        final_output = final_output + food + " " + String.valueOf(calorie) + " calories " + String.valueOf(carb) + " grams carb " + String.valueOf(sodium) + " mg sodium" + "\n";
                    }


                } catch (FileNotFoundException e) {
                  e.printStackTrace();
              } catch (IOException e) {
                  e.printStackTrace();
              } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


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

