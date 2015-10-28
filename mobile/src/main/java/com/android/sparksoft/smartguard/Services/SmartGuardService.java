package com.android.sparksoft.smartguard.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
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
    private String txtArtist = "";
    private String txtTitle = "";
    private String txtAlbum = "";

    private String lastSong = "a";
    String[] mData = new String[3];
    boolean IS_PLAYING;
    boolean INTERRUPTED;


    String url = "/storage/emulated/0/Samsung/Music/Over the Horizon.mp3";

    public static final String UPDATE_SONG = "com.wtts.custom.intent.action.UPDATE_SONG";

    private static final long UPDATE_INTERVAL = 8500;

    public static final String PHONE_STATE = "android.intent.action.PHONE_STATE";

    MediaPlayer player = new MediaPlayer();


    private int NOTIFICATION_ID = 10101;

    public static final String SHOW_PROGRESS = "com.wtts.custom.intent.action.SHOW_PROGRESS";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        if (player != null) {
            player.reset();
            player.release();
            player = null;
        }
        startPlaying();
    }

    void startPlaying() {


        try {
            player = new MediaPlayer();
            player.setDataSource(url);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer player) {
                    // TODO Auto-generated method stub
                    player.start();

                }
            });
        } catch (Exception e) {

            player.reset();
            player.release();
            player = null;

            return;
        }



    }


}
