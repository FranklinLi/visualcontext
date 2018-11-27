package com.example.franklin.visualcontext;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.util.List;

public class PreferencePageActivity extends AppCompatActivity implements View.OnClickListener{

    File root = android.os.Environment.getExternalStorageDirectory();
    File dir = new File (root.getAbsolutePath());
    File file;
    public static String message = "";
    public static final String EXTRA_MESSAGE = "extra_message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_page);

        Intent intent = getIntent();
        message = intent.getStringExtra(PreferencePageActivity.EXTRA_MESSAGE);

        Button one = (Button) findViewById(R.id.button9);
        one.setOnClickListener(this);

    }

    public void onClick(View view){

        switch (view.getId()) {

            case R.id.button9:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }




    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton:
                if (checked)
                file = new File(dir, "preference.json");
                try {
                    BufferedReader input = new BufferedReader(new FileReader(file));
                    String line;
                    StringBuilder text = new StringBuilder();
                    while ((line = input.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }

                    JSONObject jsonObj = new JSONObject(text.toString());
                    jsonObj.put(message,"like strongly");

                    FileOutputStream f = new FileOutputStream(file);
                    PrintWriter pw = new PrintWriter(f);
                    pw.println(jsonObj.toString());
                    pw.flush();
                    pw.close();
                    f.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                    // Do something to recover ... or kill the app.
                }
                break;
            case R.id.radioButton2:
                if (checked)

                    file = new File(dir, "preference.json");
                try {
                    BufferedReader input = new BufferedReader(new FileReader(file));
                    String line;
                    StringBuilder text = new StringBuilder();
                    while ((line = input.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }

                    JSONObject jsonObj = new JSONObject(text.toString());
                    jsonObj.put(message,"like");

                    FileOutputStream f = new FileOutputStream(file);
                    PrintWriter pw = new PrintWriter(f);
                    pw.println(jsonObj.toString());
                    pw.flush();
                    pw.close();
                    f.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                    // Do something to recover ... or kill the app.
                }

                    break;
            case R.id.radioButton4:
                if (checked)

                    file = new File(dir, "preference.json");
                try {
                    BufferedReader input = new BufferedReader(new FileReader(file));
                    String line;
                    StringBuilder text = new StringBuilder();
                    while ((line = input.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }

                    JSONObject jsonObj = new JSONObject(text.toString());
                    jsonObj.put(message,"neutral");

                    FileOutputStream f = new FileOutputStream(file);
                    PrintWriter pw = new PrintWriter(f);
                    pw.println(jsonObj.toString());
                    pw.flush();
                    pw.close();
                    f.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                    // Do something to recover ... or kill the app.
                }
                    // Ninjas rule
                    break;
            case R.id.radioButton5:
                if (checked)

                    file = new File(dir, "preference.json");
                try {
                    BufferedReader input = new BufferedReader(new FileReader(file));
                    String line;
                    StringBuilder text = new StringBuilder();
                    while ((line = input.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }

                    JSONObject jsonObj = new JSONObject(text.toString());
                    jsonObj.put(message,"dislike");

                    FileOutputStream f = new FileOutputStream(file);
                    PrintWriter pw = new PrintWriter(f);
                    pw.println(jsonObj.toString());
                    pw.flush();
                    pw.close();
                    f.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                    // Do something to recover ... or kill the app.
                }

                    // Ninjas rule
                    break;
            case R.id.radioButton6:
                if (checked)

                    file = new File(dir, "preference.json");
                try {
                    BufferedReader input = new BufferedReader(new FileReader(file));
                    String line;
                    StringBuilder text = new StringBuilder();
                    while ((line = input.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }

                    JSONObject jsonObj = new JSONObject(text.toString());
                    jsonObj.put(message,"dislike strongly");

                    FileOutputStream f = new FileOutputStream(file);
                    PrintWriter pw = new PrintWriter(f);
                    pw.println(jsonObj.toString());
                    pw.flush();
                    pw.close();
                    f.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                    // Do something to recover ... or kill the app.
                }

                    // Ninjas rule
                    break;

        }
    }


}
