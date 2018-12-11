package com.example.franklin.visualcontext;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class SelfReportActivity extends AppCompatActivity implements View.OnClickListener {

    private static EditText eText;
    private static final String TAG = SelfReportActivity.class.getSimpleName();
    private SpeechRecognizer sr;
    private SpeechListener speechListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_report);
        eText = findViewById(R.id.selfreportedfood);
        Button submit = findViewById(R.id.submitbutton);
        submit.setOnClickListener(this);
        Button voice = findViewById(R.id.voicebutton);
        voice.setOnClickListener(this);
        sr = SpeechRecognizer.createSpeechRecognizer(this);
        speechListener = new SpeechListener(eText);
        sr.setRecognitionListener(speechListener);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitbutton:
                String str = eText.getText().toString();
                eText.setText("");
                new CallNutritionixAPITask(this).execute(str);
                break;
            case R.id.voicebutton:
                requestRecordAudioPermission();
                eText.setText("");
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE," com.example.franklin" +
                        ".visualcontext");
                intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                sr.startListening(intent);
                break;
            default:
                break;
        }
    }

    /**
     * Asks for record audio permission if not permitted
     */
    private void requestRecordAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String requiredPermission = Manifest.permission.RECORD_AUDIO;

            // If the user previously denied this permission then show a message explaining why
            // this permission is needed
            if (checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{requiredPermission}, 101);
            }
        }
    }
}
