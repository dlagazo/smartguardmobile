package com.android.sparksoft.smartguard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Daniel on 9/25/2015.
 */
public class VoiceRecognition implements RecognitionListener
{
    private String speechText;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private Context _context;
    SpeechBot sp;
    VoiceRecognition(Context context, String[] keywords)
    {

        _context = context;
        sp = new SpeechBot(_context);
        speech = SpeechRecognizer.createSpeechRecognizer(context);
        speech.setRecognitionListener(this);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                context.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
    }

    public void start()
    {
        Toast.makeText(_context, "Say something", Toast.LENGTH_LONG).show();
        speech.startListening(recognizerIntent);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        speech.stopListening();

    }

    public void stop() {
        speech.stopListening();
        if (speech != null) {
            speech.destroy();
        }
    }



    @Override
    public void onReadyForSpeech(Bundle params)
    {
        //Toast.makeText(_context, "Ready for speech", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {
        String errorMessage = getErrorText(error);
    }

    @Override
    public void onResults(Bundle results) {
        //Toast.makeText(_context, "Results detected", Toast.LENGTH_LONG).show();
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + " ";
        speechText = text;
        Toast.makeText(_context, text, Toast.LENGTH_LONG).show();

        if(text.toLowerCase().contains("yes"))
        {
            sp.talk("yes");
            Toast.makeText(_context, "Yes detected in speech", Toast.LENGTH_SHORT).show();
        }

        else if(text.toLowerCase().contains("no"))
            sp.talk("no");
        stop();


    }

    public Boolean processText(String[] keyword)
    {
        return false;
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}
