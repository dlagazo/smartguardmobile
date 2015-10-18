package com.android.sparksoft.smartguard.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioRecord;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.widget.Toast;

import com.android.sparksoft.smartguard.Features.FallDetector;
import com.android.sparksoft.smartguard.Features.SoundMeter;
import com.android.sparksoft.smartguard.Features.SpeechBot;

public class SmartGuardService extends Service{
    int count = 0;






    public SmartGuardService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    PowerManager pm;
    PowerManager.WakeLock wl;
    int bufferSize;
    AudioRecord audio;
    double lastLevel;
    SoundMeter sm;
    int status;
    boolean isCharging;
    FallDetector fl;
    SpeechBot sp;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        fl = new FallDetector(this);
        SharedPreferences prefs = this.getSharedPreferences("com.android.sparksoft", Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("fallStatus", "false");
        edit.commit();
        sp = new SpeechBot(getApplicationContext());
        //sm = new SoundMeter();
        //sm.start();

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        wl.acquire();

        //wl.release();

        // Let it continue running until it is stopped.

        Toast.makeText(this, "Syncing contacts", Toast.LENGTH_LONG).show();


        final Handler handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                //Toast.makeText(getApplicationContext(), "audio:" + sm.getAmplitude() + " battery:" + isCharging
                //        , Toast.LENGTH_SHORT).show();

                //Toast.makeText(getApplicationContext(), "x:" + x + "y:" + y + "z:" + z, Toast.LENGTH_SHORT).show();
                status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
                fl.resetDetector();
                /*
                if(sm.getAmplitude() > 4000 && isCharging)
                {



                    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                    wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK|PowerManager.ACQUIRE_CAUSES_WAKEUP, "bbbb");
                    wl.acquire();

                    ActivityManager manager =
                            (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
                    List<ActivityManager.RunningAppProcessInfo> processes = manager.getRunningAppProcesses();
                    for (ActivityManager.RunningAppProcessInfo process : processes)
                    {
                        if(process.pkgList[0].equalsIgnoreCase("com.android.launcher"))
                        {

                        }
                        else
                        {

                        }

                    }


                    Intent dialogIntent = new Intent(getApplicationContext(), MenuActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }

                if(count == 3)
                {

                }*/

            }

        };



        new Thread(new Runnable(){
            public void run() {
                // TODO Auto-generated method stub
                while(true)
                {

                    //Toast.makeText(getApplicationContext(), "Audio:" + sm.getAmplitude(), Toast.LENGTH_LONG).show();
                    try {
                        Thread.sleep(5000);
                        handler.sendEmptyMessage(0);

                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        }).start();
        return 0;
    }

    private void readAudioBuffer() {

        try {
            short[] buffer = new short[bufferSize];

            int bufferReadResult = 1;

            if (audio != null)
            {

// Sense the voice…
                bufferReadResult = audio.read(buffer, 0, bufferSize);
                double sumLevel = 0;
                for (int i = 0; i < bufferReadResult; i++)
                {
                    sumLevel += buffer[i];
                }
                lastLevel = Math.abs((sumLevel / bufferReadResult));
            }
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }



}
