package com.example.franklin.visualcontext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class PreferenceActivity extends AppCompatActivity implements View.OnClickListener{

    File root = android.os.Environment.getExternalStorageDirectory();
    File dir = new File (root.getAbsolutePath());
    File file;
    CheckBox peanutallergies,halal,glutenfree,vegetarian,kosher;
    String message = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        Intent intent = getIntent();

        peanutallergies=(CheckBox)findViewById(R.id.PeanutAllergies);
        halal=(CheckBox)findViewById(R.id.Halal);
        glutenfree=(CheckBox)findViewById(R.id.GlutenFree);
        vegetarian=(CheckBox)findViewById(R.id.Vegetarian);
        kosher=(CheckBox)findViewById(R.id.Kosher);

        Button one = (Button) findViewById(R.id.button7);
        one.setOnClickListener(this);
    }

    public void onClick(View view){

        switch (view.getId()) {

            case R.id.button7:
                file = new File(dir, "preference.json");

                try{
                BufferedReader input = new BufferedReader(new FileReader(file));
                String line;
                StringBuilder text = new StringBuilder();
                while ((line = input.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                JSONObject jsonObj = new JSONObject(text.toString());

                    if(peanutallergies.isChecked()){
                        jsonObj.put("peanut allergies", "yes");
                    }
                    else{
                        jsonObj.put("peanut allergies", "no");
                    }
                    if(halal.isChecked()){
                        jsonObj.put("halal", "yes");
                    }
                    else{
                        jsonObj.put("halal", "no");
                    }

                    if(kosher.isChecked()){
                        jsonObj.put("kosher", "yes");
                    }
                    else{
                        jsonObj.put("kosher", "no");
                    }

                    if(vegetarian.isChecked()){
                        jsonObj.put("vegetarian", "yes");
                    }
                    else{
                        jsonObj.put("vegetarian", "no");
                    }
                    if(glutenfree.isChecked()){
                        jsonObj.put("glutenfree", "yes");
                    }
                    else{
                        jsonObj.put("glutenfree", "no");
                    }

                    FileOutputStream f = new FileOutputStream(file);
                    PrintWriter pw = new PrintWriter(f);
                    pw.println(jsonObj.toString());
                    pw.flush();
                    pw.close();
                    f.close();

                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                    // Do something to recover ... or kill the app.
                }

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                break;
            default:
                break;
        }




    }




}
