package com.android.sparksoft.smartguard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.sparksoft.smartguard.Helpers.HelperLogin;

public class LoginActivity extends AppCompatActivity {
    private SpeechBot sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = new SpeechBot(this);

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

                HelperLogin hr = new HelperLogin(getApplicationContext(), basicAuth, sp);
                //hr.loginHelper(url);
                hr.SyncHelperJSONObject(url);
                //hr.contactsHelper(url);

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
