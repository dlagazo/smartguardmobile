package com.android.sparksoft.smartguard;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Daniel on 9/26/2015.
 */
public class SpeechBot implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;
    private Context _context;

    public SpeechBot(Context context)
    {
        _context = context;

        tts = new TextToSpeech(context, this);
        AudioManager audioManager = (AudioManager) _context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(true);
    }

    @Override
    public void onInit(int code) {
        if (code == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.ENGLISH);
            float rate = 0.8f;
            tts.setSpeechRate(rate);

            tts.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {

                @Override
                public void onUtteranceCompleted(String utteranceId) {

                    if (tts != null) {
                        //tts.stop();

                        //tts.shutdown();
                        //tts = null;
                        //Toast.makeText(_context, "Speech bot shutting down",
                        //        Toast.LENGTH_SHORT).show();
                    }
                }
            });


        } else {
            tts = null;
            Toast.makeText(_context, "Failed to initialize TTS engine.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void talk(String line)
    {
        if (!tts.isSpeaking()) {
            tts.speak(line, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void destroy()
    {
        if (tts != null) {
            tts.stop();


            tts.shutdown();
        }
    }

    



}
