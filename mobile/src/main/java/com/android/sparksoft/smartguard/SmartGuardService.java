package com.android.sparksoft.smartguard;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.widget.Toast;

import java.util.List;

public class SmartGuardService extends Service implements SensorEventListener{
    int count = 0;
    float x, y, z;
    private SensorManager sensorManager;
    private Sensor accelerometer;


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

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {



        sm = new SoundMeter();
        sm.start();

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        wl.acquire();

        //wl.release();

        // Let it continue running until it is stopped.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

            //vibrateThreshold = accelerometer.getMaximumRange() / 2;

        }
        Toast.makeText(this, "Service recording", Toast.LENGTH_LONG).show();

        final Handler handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                Toast.makeText(getApplicationContext(), "audio:" + sm.getAmplitude() + " battery:" + isCharging
                        , Toast.LENGTH_SHORT).show();

                //Toast.makeText(getApplicationContext(), "x:" + x + "y:" + y + "z:" + z, Toast.LENGTH_SHORT).show();
                status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
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
                        Thread.sleep(1000);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];
        if((Math.abs(x) + Math.abs(y) + Math.abs(z)) > 40)
        {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK|PowerManager.ACQUIRE_CAUSES_WAKEUP, "bbbb");
            wl.acquire();




            Intent dialogIntent = new Intent(getApplicationContext(), MenuActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialogIntent);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
