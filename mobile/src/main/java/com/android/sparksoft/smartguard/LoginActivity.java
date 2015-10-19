package com.android.sparksoft.smartguard;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.sparksoft.smartguard.Database.DataSourceSettings;
import com.android.sparksoft.smartguard.Helpers.HelperLogin;
import com.android.sparksoft.smartguard.Features.SpeechBot;
import com.android.sparksoft.smartguard.Models.Settings;

public class LoginActivity extends AppCompatActivity {
    private SpeechBot sp;
    private HelperLogin hr;
    private DataSourceSettings dsSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = new SpeechBot(this);
        /*
        dsSettings = new DataSourceSettings(getApplicationContext());
        if(dsSettings.getAllSettings()!= null) {
            for (Settings set : dsSettings.getAllSettings()) {
                if (set.getKey().equals("isValid")) {
                    if (set.getValue().equals("true")) {
                        Intent myIntent = new Intent(getApplicationContext(), MenuActivity.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myIntent);
                        finish();
                    }
                }
            }
        }
        */
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.talk("Logging in. Please wait.");
                Toast.makeText(getApplicationContext(), "Logging in. Please wait.", Toast.LENGTH_LONG).show();

                TextView username = (TextView)findViewById(R.id.etUsername);
                TextView password = (TextView)findViewById(R.id.etPassword);

                String url = "http://smartguardportal.azurewebsites.net/api/MobileContact";



                final String basicAuth = "Basic " + Base64.encodeToString((username.getText() + ":" +
                        password.getText()).getBytes(), Base64.NO_WRAP);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                hr = new HelperLogin(getApplicationContext(), basicAuth, sp);
                hr.SyncHelperJSONObject(url);



                /*
                try {
                    Thread.sleep(5000);
                    if(hr.getResult())
                    {
                        Intent myIntent = new Intent(getApplicationContext(), MenuActivity.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myIntent);
                        finish();
                    }
                    else
                    {
                        //sp.talk("Login failed. Please try again.");
                        //Toast.makeText(getApplicationContext(), "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                */
                //hr.loginHelper(url);

                /*
                final Handler loginHandler = new Handler()
                {
                    @Override
                    public void handleMessage(Message msg) {
                        // TODO Auto-generated method stub
                        super.handleMessage(msg);
                            if(hr.getResult())
                            {
                                //setContentView(R.layout.activity_menu);
                                Intent myIntent = new Intent(getApplicationContext(), MenuActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(myIntent);
                                finish();


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
                                Thread.sleep(10000);
                                loginHandler.sendEmptyMessage(0);

                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                    }
                }).start();

                */


            }

        });




    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
