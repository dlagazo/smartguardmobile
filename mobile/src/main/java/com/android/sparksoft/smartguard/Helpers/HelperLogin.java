package com.android.sparksoft.smartguard.Helpers;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.sparksoft.smartguard.MenuActivity;
import com.android.sparksoft.smartguard.SmartGuardService;
import com.android.sparksoft.smartguard.SpeechBot;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 10/7/2015.
 */
public class HelperLogin {

    private String basicAuth;
    private SpeechBot sp;
    private Context context;

    public HelperLogin(Context _context, String _basicAuth, SpeechBot _sp)
    {
        basicAuth = _basicAuth;
        context = _context;
        sp = _sp;
    }

    public void loginHelper(final String url)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONArray params = new JSONArray();

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
                                    Toast.makeText(context, "Login successful", Toast.LENGTH_LONG).show();
                                    sp.talk("Login successful");
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    //sp.destroy();
                                    sp.talk("Getting my contact's information");
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    Intent myIntent = new Intent(context, MenuActivity.class);
                                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    //myIntent.putExtra("key", value); //Optional parameters
                                    context.startService(new Intent(context, SmartGuardService.class));
                                    context.startActivity(myIntent);


                                }
                                else
                                {
                                    //Toast.makeText(context, "Login failed. Please try again", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(context, "Response Code: " +
                                    //error.networkResponse.headers.get("X-Android-Response-Source") + " " +
                                    error.networkResponse.statusCode, Toast.LENGTH_LONG).show();
                            if(error.networkResponse.statusCode == 401) {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(context, "Invalid username or password", Toast.LENGTH_LONG).show();
                                sp.talk("Wrong username or password. Please try again.");
                            }
                            //}
                        }
                        else
                        {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
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

    public void contactsHelper(final String url)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONArray params = new JSONArray();

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
                                Toast.makeText(context, obj.getString("Mobile"), Toast.LENGTH_SHORT).show();


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
                            Toast.makeText(context, "Response Code: " +
                                    //error.networkResponse.headers.get("X-Android-Response-Source") + " " +
                                    error.networkResponse.statusCode, Toast.LENGTH_LONG).show();
                            if(error.networkResponse.statusCode == 401) {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(context, "Invalid username or password", Toast.LENGTH_LONG).show();
                                sp.talk("Wrong username or password. Please try again.");
                            }
                            //}
                        }
                        else
                        {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
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
}
