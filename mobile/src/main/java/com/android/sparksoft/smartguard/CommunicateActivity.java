package com.android.sparksoft.smartguard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.sparksoft.smartguard.Database.DataSourceContacts;
import com.android.sparksoft.smartguard.Features.SpeechBot;
import com.android.sparksoft.smartguard.Features.VoiceRecognition;
import com.android.sparksoft.smartguard.Listeners.CallListener;
import com.android.sparksoft.smartguard.Models.Contact;
import com.android.sparksoft.smartguard.Services.FallService;

import java.util.ArrayList;
import java.util.Locale;

public class CommunicateActivity extends AppCompatActivity {
    VoiceRecognition vr;
    SpeechBot sp;
    ArrayList<Contact> arrayContacts;
    SharedPreferences sharedPrefs;
    private DataSourceContacts dsContacts;
    private static final int VOICE_RECOGNITION = 1;
    int status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contacts);


        dsContacts = new DataSourceContacts(this);
        dsContacts.open();

        sp = new SpeechBot(this, "Who do you want to call?");
        speak();
        arrayContacts = dsContacts.getAllContacts();

        LinearLayout ll = (LinearLayout)findViewById(R.id.linearlayoutContacts);
        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ll.getWidth(), ll.getHeight()/5);
        for (final Contact con:dsContacts.getAllContacts())
        {
            Button btn = new Button(this);
            //btn.setHeight(0);
            btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    0, 1f));
            //btn.setWidth(ll.getWidth());
            btn.setText(con.getFullName());

            ll.addView(btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //CallListener cl = new CallListener(getApplicationContext(), sp, arrayContacts
                    AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                    audioManager.setSpeakerphoneOn(true);
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + con.getMobile()));
                    startActivity(intent);



                }
            });
        }
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
            for(Contact con:dsContacts.getAllContacts()) {
                for (String str : results.get(0).split(" ")) {
                    if (str.toLowerCase().equals(con.getFirstName().toLowerCase()))
                    {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + con.getMobile()));
                        startActivity(intent);
                        finish();
                    }

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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


}
