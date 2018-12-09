package com.example.franklin.visualcontext;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.franklin.visualcontext.data.nutrition.AggregateNutritionData;
import com.example.franklin.visualcontext.data.nutrition.NutritionixInfoAcquirer;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SelfReportActivity extends AppCompatActivity implements View.OnClickListener {

    private static EditText eText;
    private static Button btn;
    private static final String TAG = SelfReportActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_report);
        eText = (EditText) findViewById(R.id.selfreportedfood);
        btn = (Button) findViewById(R.id.submitbutton);
        btn.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitbutton:
                String str = eText.getText().toString();
                eText.setText("");
                new CallNutritionixAPITask(this).execute(str);
                break;
            case R.id.voicebutton:
                
            default:
                break;
        }
    }


}
