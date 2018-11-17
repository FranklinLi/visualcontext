package com.example.franklin.visualcontext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

                if(peanutallergies.isChecked()){
                    message = message + "peanutallergies,";
                }
                if(halal.isChecked()){
                    message = message + "halal,";
                }
                if(kosher.isChecked()){
                    message = message + "kosher,";
                }
                if(vegetarian.isChecked()){
                    message = message + "vegetarian,";
                }
                if(glutenfree.isChecked()){
                    message = message + "glutenfree";
                }
                file = new File(dir, "restriction.txt");
                try {
                    FileOutputStream f = new FileOutputStream(file);
                    PrintWriter pw = new PrintWriter(f);
                    pw.println(message);
                    pw.flush();
                    pw.close();
                    f.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                message = "";
                break;
            default:
                break;
        }




    }




}
