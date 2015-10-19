package com.android.sparksoft.smartguard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.sparksoft.smartguard.Services.SmartGuardService;

import java.util.ArrayList;
import java.util.Locale;

public class MenuActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_menu);



        startService(new Intent(getApplicationContext(), FallService.class));


        setButtons();
        dsContacts = new DataSourceContacts(this);
        dsContacts.open();

        sharedPrefs = getSharedPreferences("prefs", Context.MODE_WORLD_WRITEABLE);

        sp = new SpeechBot(this);
        arrayContacts = dsContacts.getAllContacts();


    }

    Button btnSos, btnSosCall, btnSosOk;

    public void setButtons()
    {
        btnSos = (Button)findViewById(R.id.btnSos);
        btnSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.layout_sos);
                status = 0;
                btnSosOk = (Button)findViewById(R.id.btnSosOk);
                btnSosOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.activity_menu);
                        setButtons();
                    }
                });
                btnSosCall = (Button)findViewById(R.id.btnSosCall);
                btnSosCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.activity_menu);

                        CallListener pscl = new CallListener(getApplicationContext(), sp, arrayContacts);
                        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                        tm.listen(pscl, PhoneStateListener.LISTEN_CALL_STATE);

                        //String mobile = hr.getContactsList().get(0).getMobile();
                        //Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+639989576763"));
                        //startActivity(intent);

                        setButtons();
                    }
                });
                //Button[] btns = {btnSosOk, btnSosCall};
                sp.talk("Do you need an emergency call?");
                Toast.makeText(getApplicationContext(),"Do you need an emergency call?", Toast.LENGTH_LONG).show();
                //vr = new VoiceRecognition(getApplicationContext(), btns, null);
                //vr.setCommand(0);
                //vr.start();
                speak();


                //0-emergency

                /*
                CallListener pscl = new CallListener(getApplicationContext(), sp, arrayContacts);
                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                tm.listen(pscl, PhoneStateListener.LISTEN_CALL_STATE);

                //String mobile = hr.getContactsList().get(0).getMobile();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+639989576763"));
                startActivity(intent);
                */


            }
        });

        Button btnContacts = (Button) findViewById(R.id.btnContacts);
        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.layout_contacts);
                LinearLayout layoutContacts = (LinearLayout) findViewById(R.id.linearlayoutContacts);

                for (Contact contact : dsContacts.getAllContacts()) {
                    Button btnNewContact = new Button(getApplicationContext());
                    btnNewContact.setWidth(layoutContacts.getWidth());
                    btnNewContact.setHeight(layoutContacts.getHeight() / 5);
                    btnNewContact.setText(contact.getFullName());
                    layoutContacts.addView(btnNewContact);
                }

            }
        });

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
                if(str.toLowerCase().equals("yes") && status == 0)
                {
                    btnSosCall.performClick();
                }
                else if(str.toLowerCase().equals("ok") && status == 0)
                {
                    btnSosOk.performClick();
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
