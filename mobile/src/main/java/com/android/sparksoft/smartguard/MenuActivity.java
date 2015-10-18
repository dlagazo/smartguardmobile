package com.android.sparksoft.smartguard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
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

import com.android.sparksoft.smartguard.Features.SpeechBot;
import com.android.sparksoft.smartguard.Features.VoiceRecognition;
import com.android.sparksoft.smartguard.Listeners.CallListener;
import com.android.sparksoft.smartguard.Models.Contact;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    VoiceRecognition vr;
    SpeechBot sp;
    ArrayList<Contact> arrayContacts;
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sharedPrefs = getSharedPreferences("prefs", Context.MODE_WORLD_WRITEABLE);

        sp = new SpeechBot(this);
        arrayContacts = new ArrayList<Contact>();

        Button btnCall = (Button)findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CallListener pscl = new CallListener(getApplicationContext(), sp, arrayContacts);
                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                tm.listen(pscl, PhoneStateListener.LISTEN_CALL_STATE);

                //String mobile = hr.getContactsList().get(0).getMobile();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+639989576763"));
                startActivity(intent);

                final Handler loginHandler = new Handler(){

                    @Override
                    public void handleMessage(Message msg) {
                        // TODO Auto-generated method stub
                        super.handleMessage(msg);
                        Toast.makeText(getApplicationContext(), sharedPrefs.getString("callState", "default"), Toast.LENGTH_SHORT).show();

                        if(sharedPrefs.getString("callState", "failed").equals("unanswered"))
                        {
                            //setContentView(R.layout.activity_menu);
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+639989576763"));
                            startActivity(intent);


                            //startService(new Intent(getApplicationContext(), SmartGuardService.class));


                        }

                    }

                };

                new Thread(new Runnable(){
                    public void run() {
                        // TODO Auto-generated method stub
                        while(true)
                        {

                            //Toast.makeText(getApplicationContext(), "Audio:" + sm.getAmplitude(), Toast.LENGTH_LONG).show();
                            try {
                                Thread.sleep(30000);
                                loginHandler.sendEmptyMessage(0);

                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                    }
                }).start();

            }
        });

        Button btnContacts = (Button)findViewById(R.id.btnContacts);
        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.layout_contacts);
                LinearLayout layoutContacts = (LinearLayout)findViewById(R.id.linearlayoutContacts);
                /*
                for (Contact contact:hr.getContactsList() )
                {
                    Button btnNewContact = new Button(getApplicationContext());
                    btnNewContact.setWidth(layoutContacts.getWidth());
                    btnNewContact.setHeight(layoutContacts.getHeight()/5);
                    btnNewContact.setText(contact.getFullName());
                    layoutContacts.addView(btnNewContact);
                }
                */
            }
        });

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
