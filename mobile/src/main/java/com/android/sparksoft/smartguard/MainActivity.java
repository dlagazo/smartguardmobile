package com.android.sparksoft.smartguard;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.sparksoft.smartguard.Database.DataSourceContacts;
import com.android.sparksoft.smartguard.Features.SpeechBot;
import com.android.sparksoft.smartguard.Features.VoiceRecognition;
import com.android.sparksoft.smartguard.Listeners.CallListener;
import com.android.sparksoft.smartguard.Services.FallService;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech tts;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private VoiceRecognition vr;
    private DataSourceContacts dsContacts;
    private SpeechBot sp;
    private static final int VOICE_RECOGNITION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        dsContacts = new DataSourceContacts(this);
        dsContacts.open();
        sp = new SpeechBot(this, null);
        speak();

        setContentView(R.layout.layout_sos);

        //promptSpeechInput();

        Button btnSOSCall = (Button)findViewById(R.id.btnSosCall);
        btnSOSCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallListener pscl = new CallListener(getApplicationContext(), sp, dsContacts.getAllContacts());
                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                tm.listen(pscl, PhoneStateListener.LISTEN_CALL_STATE);
                //startService(new Intent(getApplicationContext(), FallService.class));
            }
        });

        Button btnSOSOk = (Button)findViewById(R.id.btnSosOk);
        btnSOSOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startService(new Intent(getApplicationContext(), FallService.class));
                finish();
            }
        });

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (tts != null) {
            tts.stop();


            tts.shutdown();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        if (requestCode == VOICE_RECOGNITION && resultCode == RESULT_OK) {
            ArrayList<String> results;
            results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            // TODO Do something with the recognized voice strings

            Toast.makeText(this, results.get(0), Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), results.get(0), Toast.LENGTH_LONG).show();
            for(String str:results.get(0).split(" "))
            {
                if(str.toLowerCase().equals("yes"))
                {
                    Toast.makeText(getApplicationContext(),"Emergency call", Toast.LENGTH_SHORT).show();
                }
                else if(str.toLowerCase().equals("ok") || str.toLowerCase().equals("no"))
                {
                    finish();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Specify free form input
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Please start speaking");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        startActivityForResult(intent, VOICE_RECOGNITION);
    }

}
