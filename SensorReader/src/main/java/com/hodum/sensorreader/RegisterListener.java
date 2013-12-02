package com.hodum.sensorreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * Created by fhodum on 11/12/13.
 */
public class RegisterListener extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, SensorService.class);
        context.startService(i);
    }
}
