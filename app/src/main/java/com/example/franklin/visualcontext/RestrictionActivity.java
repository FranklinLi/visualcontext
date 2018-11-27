package com.example.franklin.visualcontext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.Buffer;


public class RestrictionActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_MESSAGE = "extra_message";
    public BufferedWriter bufferedWriter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restriction);
        Intent intent = getIntent();

        Button pork = (Button) findViewById(R.id.button);
        pork.setOnClickListener(this); // calling onClick() method
        Button chicken = (Button) findViewById(R.id.button4);
        chicken.setOnClickListener(this);
        Button beef = (Button) findViewById(R.id.button5);
        beef.setOnClickListener(this); // calling onClick() method
        Button seafood = (Button) findViewById(R.id.button6);
        seafood.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button:
                Intent intent = new Intent(this, PreferencePageActivity.class);
                String message = "pork";
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
                break;
            case R.id.button4:
                intent = new Intent(this, PreferencePageActivity.class);
                message = "chicken";
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
                // do your code
                break;
            case R.id.button5:
                intent = new Intent(this, PreferencePageActivity.class);
                message = "beef";
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
                // do your code
                break;
            case R.id.button6:
                intent = new Intent(this, PreferencePageActivity.class);
                message = "seafood";
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
                // do your code
                break;




            default:
                break;
        }
    }
}
