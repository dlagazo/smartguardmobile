package com.android.sparksoft.smartguard.Helpers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

import com.android.sparksoft.smartguard.Database.DataSourceContacts;
import com.android.sparksoft.smartguard.Features.SpeechBot;
import com.android.sparksoft.smartguard.MenuActivity;
import com.android.sparksoft.smartguard.Models.Contact;
import com.android.sparksoft.smartguard.R;
import com.android.sparksoft.smartguard.Services.SmartGuardService;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 10/7/2015.
 */
public class HelperLogin {

    private String basicAuth;
    private SpeechBot sp;
    private Context context;
    private boolean result;
    private ArrayList<Contact> contactsArray;
    private DataSourceContacts dsContacts;

    public HelperLogin(Context _context, String _basicAuth, SpeechBot _sp)
    {
        basicAuth = _basicAuth;
        context = _context;
        sp = _sp;
        result = false;
        //contactsArray = new ArrayList<Contact>();
        dsContacts = new DataSourceContacts(context);
        dsContacts.open();
    }

    public boolean getResult()
    {
        return result;
    }

    public ArrayList<Contact> getContactsList()
    {
        return contactsArray;
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

    public void SyncHelperJSONObject(String url)
    {
        //Toast.makeText(context, "Sending JSON request.", Toast.LENGTH_LONG).show();
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        //params.put("token", "AbCdEfGh123456");
        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray contacts = null, memories = null, places = null, responses = null;
                        String fullname = " ";
                        try {
                            responses = response.getJSONArray("responses");
                            for(int i= 0; i < responses.length(); i++)
                            {
                                if (responses.getJSONObject(i).getString("response").equals("Name"))
                                    fullname = responses.getJSONObject(i).getString("value");
                                else if(responses.getJSONObject(i).getString("response").equals("Result")) {
                                    Intent myIntent = new Intent(context, MenuActivity.class);
                                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(myIntent);

                                    //result = true;

                                }
                            }
                            contacts = response.getJSONArray("contacts");

                            for(int i=0; i < contacts.length(); i++)
                            {
                                //Toast.makeText(context, contacts.getJSONObject(i).get("Mobile").toString(), Toast.LENGTH_LONG).show();
                                Contact tempContact = new Contact(contacts.getJSONObject(i).getInt("ContactId"),
                                        contacts.getJSONObject(i).getString("FirstName"),
                                            contacts.getJSONObject(i).getString("LastName"),
                                                contacts.getJSONObject(i).getString("Email"),
                                                        contacts.getJSONObject(i).getString("Mobile"),
                                                                contacts.getJSONObject(i).getString("Relationship"),
                                                                        contacts.getJSONObject(i).getInt("Rank"));
                                //contactsArray.add(tempContact);
                                try
                                {
                                    dsContacts.deleteContact(contacts.getJSONObject(i).getInt("ContactId"));
                                }
                                catch (Exception ex)
                                {

                                }
                                dsContacts.createContact(tempContact);

                            }
                            memories = response.getJSONArray("memories");
                            for(int i=0; i < memories.length(); i++)
                            {
                                //Toast.makeText(context, memories.getJSONObject(i).get("MemoryName").toString(), Toast.LENGTH_LONG).show();
                            }
                            places = response.getJSONArray("places");
                            for(int i=0; i< places.length(); i++)
                            {
                                //Toast.makeText(context, places.getJSONObject(i).get("PlaceName").toString(), Toast.LENGTH_LONG).show();
                            }
                            Vibrator v = (Vibrator) context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(500);

                            sp.talk("Hello " + fullname + ". You have " + contacts.length() + " contacts, " +
                                    memories.length() + " memories, " +
                                    places.length() + " places.");
                            Toast.makeText(context, "Hello " + fullname + ". You have " + contacts.length() + " contacts, " +
                                    memories.length() + " memories, " +  places.length() + " places.", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                })
                {

                /**
                 * Passing some request headers
                 * */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError
                    {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Authorization", basicAuth);
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }

                };

                req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(req);
    }
}
