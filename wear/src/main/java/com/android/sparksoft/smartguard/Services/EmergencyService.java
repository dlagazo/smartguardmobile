package com.android.sparksoft.smartguard.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class EmergencyService extends Service {
    public EmergencyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(final Intent intent, int flags, int startId) {

        return 0;
    }
}
