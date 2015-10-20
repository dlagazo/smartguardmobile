package com.android.sparksoft.smartguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.sparksoft.smartguard.Database.DataSourceContacts;
import com.android.sparksoft.smartguard.Database.DataSourcePlaces;
import com.android.sparksoft.smartguard.Features.SpeechBot;
import com.android.sparksoft.smartguard.Features.VoiceRecognition;
import com.android.sparksoft.smartguard.Models.Contact;
import com.android.sparksoft.smartguard.Models.Place;

import java.util.ArrayList;
import java.util.Locale;

public class NavigateActivity extends AppCompatActivity {
    VoiceRecognition vr;
    SpeechBot sp;
    ArrayList<Place> arrayPlaces;
    SharedPreferences sharedPrefs;
    private DataSourcePlaces dsPlaces;
    private static final int VOICE_RECOGNITION = 1;
    int status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contacts);



        dsPlaces = new DataSourcePlaces(this);
        dsPlaces.open();

        sp = new SpeechBot(this, "Where do you want to go?");
        speak();

        arrayPlaces = dsPlaces.getAllPlaces();

        LinearLayout ll = (LinearLayout)findViewById(R.id.linearlayoutContacts);
        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ll.getWidth(), ll.getHeight()/5);

        for (final Place place:dsPlaces.getAllPlaces())
        {
            Button btn = new Button(this);
            //btn.setHeight(0);
            btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    0, 1f));
            //btn.setWidth(ll.getWidth());
            btn.setText(place.getPlaceName());

            ll.addView(btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=14.672,121.022&daddr="+
                                            place.getPlaceLat() + "," + place.getPlaceLong()));
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

            for(Place place: dsPlaces.getAllPlaces()) {
                for (String str : results.get(0).split(" ")) {
                    if (str.toLowerCase().equals(place.getPlaceName().toLowerCase()))
                    {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=14.672,121.022&daddr="+
                                        place.getPlaceLat() + "," + place.getPlaceLong()));
                        startActivity(intent);
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
