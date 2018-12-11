package com.example.franklin.visualcontext;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Used with Google SpeechRecognizer for speech recognition
 */
@AllArgsConstructor
public class SpeechListener implements RecognitionListener {

    private static final String TAG = RecognitionListener.class.getSimpleName();

    /**
     * Represents the text of user's speech
     */
    @Getter
    private TextView inputView;

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d(TAG, "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.d(TAG, "onRmsChanged");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d(TAG, "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "onEndofSpeech");
    }

    @Override
    public void onError(int error) {
        Log.d(TAG,  "error " +  error);
    }

    @Override
    public void onResults(Bundle results) {
        Log.d(TAG, "onResults " + results);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        String inputSoFar = "";
        List data = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (int i = 0; i < data.size(); i++)
        {
            Log.d(TAG, "result " + data.get(i));
            inputSoFar += data.get(i);
        }
        inputView.setText(inputSoFar);
        Log.d(TAG, "onPartialResults");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, "onEvent " + eventType);
    }
}
