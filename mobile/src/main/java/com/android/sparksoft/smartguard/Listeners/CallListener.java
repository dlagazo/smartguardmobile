package com.android.sparksoft.smartguard.Listeners;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.sparksoft.smartguard.Models.Contact;
import com.android.sparksoft.smartguard.Features.SpeechBot;

import java.util.ArrayList;

/**
 * Created by Daniel on 10/18/2015.
 */
public class CallListener extends PhoneStateListener {

    private Context context;
    private SpeechBot sp;
    private boolean didHook;
    private boolean didRing;
    private ArrayList<Contact> contacts;
    private int callCount;
    private SharedPreferences sharedPrefs;

    public CallListener(Context _context, SpeechBot _sp, ArrayList<Contact> _contacts)
    {
        callCount = 0;
        context = _context;
        sp = _sp;
        didHook = false;
        didRing = false;
        contacts = _contacts;
        sharedPrefs = _context.getSharedPreferences("prefs", Context.MODE_WORLD_WRITEABLE);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contacts.get(0).getMobile()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        if(TelephonyManager.CALL_STATE_RINGING == state) {
            //Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            Toast.makeText(context, "Call state is ringing.", Toast.LENGTH_LONG).show();

            editor.putString("callState", "ringing");
            didRing = true;
        }
        if(TelephonyManager.CALL_STATE_OFFHOOK == state) {
            //wait for phone to go offhook (probably set a boolean flag) so you know your app initiated the call.
            Toast.makeText(context, "Call state is offhook.", Toast.LENGTH_LONG).show();
            editor.putString("callState", "offhook");
            didHook = true;


        }
        if(TelephonyManager.CALL_STATE_IDLE == state) {
            //when this state occurs, and your flag is set, restart your app
            Toast.makeText(context, "Call state is idle.", Toast.LENGTH_LONG).show();
            if(!didRing && didHook)
            {
                callCount++;
                if(callCount < contacts.size())
                {

                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contacts.get(callCount).getMobile()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }


                /*
                String mobile = contacts.get(0).getMobile();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                */

            }
        }

    }
}
