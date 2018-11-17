package com.example.franklin.visualcontext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button one = (Button) findViewById(R.id.button2);
        one.setOnClickListener(this); // calling onClick() method
        Button two = (Button) findViewById(R.id.button3);
        two.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

                case R.id.button3:
                    Intent intent = new Intent(this, PreferenceActivity.class);
                    startActivity(intent);
                break;
                case R.id.button2:
                    intent = new Intent(this, RestrictionActivity.class);
                    startActivity(intent);
                // do your code
                break;

            default:
                break;
        }
    }
}
