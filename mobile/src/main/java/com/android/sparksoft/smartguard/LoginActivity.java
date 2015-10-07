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
                sp.talk("Logging in. This may take a few seconds. Please wait.");
                Toast.makeText(getApplicationContext(), "Logging in. This may take a few seconds. Please wait.", Toast.LENGTH_LONG).show();

                TextView username = (TextView)findViewById(R.id.etUsername);
                TextView password = (TextView)findViewById(R.id.etPassword);

                String url = "http://smartguardportal.azurewebsites.net/api/Mobile?user=" + username.getText().toString();



                final String basicAuth = "Basic " + Base64.encodeToString((username.getText() + ":" +
                        password.getText()).getBytes(), Base64.NO_WRAP);


                HelperLogin hr = new HelperLogin(getApplicationContext(), basicAuth, sp);
                hr.loginHelper(url);


                /*
                JSONArray params = new JSONArray();


//

                JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                        url,
                        new Response.Listener<JSONArray>()
                        {

                            @Override
                            public void onResponse(JSONArray response)
                            {
                                //Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG).show();
                                for (int i = 0; i < response.length(); i++) {
                                    try {

                                        JSONObject obj = response.getJSONObject(i);
                                        if(obj.getString("response").equals("Result") && obj.getString("value").equals("Success"))
                                        {
                                            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                                            sp.talk("Login successful");
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            sp.destroy();

                                            Intent myIntent = new Intent(getApplicationContext(), MenuActivity.class);
                                            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            //myIntent.putExtra("key", value); //Optional parameters
                                            startService(new Intent(getBaseContext(), SmartGuardService.class));
                                            getApplicationContext().startActivity(myIntent);
                                            finish();
                                        }
                                        else
                                        {
                                            //Toast.makeText(getApplicationContext(), "Login failed. Please try again", Toast.LENGTH_LONG).show();
                                            //sp.talk("Login failed. Please try again");
                                        }


                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        },
                        new Response.ErrorListener()
                        {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

                                if(error.networkResponse!= null) {

                                    //for (String str : error.networkResponse.headers.keySet()) {
                                        Toast.makeText(getApplicationContext(), "Response Code: " +
                                                //error.networkResponse.headers.get("X-Android-Response-Source") + " " +
                                                error.networkResponse.statusCode, Toast.LENGTH_LONG).show();
                                        if(error.networkResponse.statusCode == 401) {
                                            try {
                                                Thread.sleep(2000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
                                            sp.talk("Wrong username or password. Please try again.");
                                        }
                                    //}
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }

                        })
                        {


                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("Authorization", basicAuth);
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                return headers;
                            }

                        };
                jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                queue.add(jsonObjReq);
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
