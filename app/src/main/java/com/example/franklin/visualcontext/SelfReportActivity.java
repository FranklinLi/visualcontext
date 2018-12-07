package com.example.franklin.visualcontext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.franklin.visualcontext.data.restaurant.PreferenceIngredient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.franklin.visualcontext.Constants.JSON_PREFERENCES_KEY;
import static com.example.franklin.visualcontext.Constants.JSON_RESTRICTIONS_KEY;

public class SelfReportActivity extends AppCompatActivity implements View.OnClickListener {

    EditText eText;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_report);
        eText = (EditText) findViewById(R.id.edittext);
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                String str = eText.getText().toString();
                /*
                 **    Please call the API here
                 */
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String date_json = date + ".json";
                File file = new File(getApplicationContext().getFilesDir(), date_json);
                try {
                    /*
                     ** Modify here for input.
                     */
                    WriteToReport.WriteToJsonReport(file,100,10,100,str);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                break;

            default:
                break;
        }


    }

}
