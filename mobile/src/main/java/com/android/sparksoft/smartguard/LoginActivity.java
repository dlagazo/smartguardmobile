package com.android.sparksoft.smartguard;

import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView username = (TextView)findViewById(R.id.etUsername);
                TextView password = (TextView)findViewById(R.id.etPassword);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://smartguardportal.azurewebsites.net/api/Mobile?user=" + username.getText().toString();



                final String basicAuth = "Basic " + Base64.encodeToString((username.getText() + ":" +
                        password.getText()).getBytes(), Base64.NO_WRAP);



                /*
                try{
                    HttpClient httpclient = new DefaultHttpClient();

                    HttpGet request = new HttpGet();
                    URI website = new URI("http://alanhardin.comyr.com/matt24/matt28.php");
                    request.setURI(website);
                    HttpResponse response = httpclient.execute(request);
                    in = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));

                    // NEW CODE
                    String line = in.readLine();
                    textv.append(" First line: " + line);
                    // END OF NEW CODE

                    textv.append(" Connected ");
                }catch(Exception e){
                    Log.e("log_tag", "Error in http connection "+e.toString());
                }
                */

                Toast.makeText(getApplicationContext(), basicAuth, Toast.LENGTH_LONG).show();
                /*
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response:", response);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }


                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                for(String str: error.networkResponse.headers.keySet() ) {
                                    Toast.makeText(getApplicationContext(), str + ": " +
                                           error.networkResponse.headers.get(str) , Toast.LENGTH_LONG).show();
                                }

                                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }


                )
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user", "dangelo");


                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                        Map<String, String>  params = new HashMap<String, String>();

                        //params.put("Accept", "");
                        params.put("Accept-Encoding", "gzip/deflate");
                        params.put("Authorization", "Basic ZGFuZ2VsbzplY2hlbG8=");
                        params.put("User-Agent", "runscope/0.1");
                        params.put("Content-Type", "application/json; charset=utf-8");
                        params.put("Accept-Language", "fil,fil-PH;q=0.8,tl;q=0.6,en-US;q=0.4,en;q=0.2");
                        params.put("Connection", "keep-alive");


                        return params;
                    }
                };

                */

                JSONArray params = new JSONArray();


//

                JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                        url,
                        new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG).show();
                                for (int i = 0; i < response.length(); i++) {
                                    try {

                                        JSONObject obj = response.getJSONObject(i);
                                        Toast.makeText(getApplicationContext(), "id:" + obj.getString("id") +
                                            " value:" + obj.getString("value"), Toast.LENGTH_LONG).show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }, new Response.ErrorListener()
                        {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

                                if(error.networkResponse!= null) {

                                    for (String str : error.networkResponse.headers.keySet()) {
                                        Toast.makeText(getApplicationContext(), str + ": " +
                                                error.networkResponse.headers.get(str), Toast.LENGTH_LONG).show();

                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }

                        })
                        {

                            /**
                             * Passing some request headers
                             * */
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

            }
        });


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
