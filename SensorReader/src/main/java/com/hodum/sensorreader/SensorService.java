package com.hodum.sensorreader;

import android.app.IntentService;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;


/**
 * Created by fhodum on 11/12/13.
 */
public class SensorService extends Service {

    private StepCountListener counter;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SensorManager mgr = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        Sensor stepCounter = mgr.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepCounter != null){
            // Success! There's a step sensor.
            if(counter ==null)
            {
                counter = new StepCountListener(this);

                mgr.registerListener(counter,stepCounter,SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
        else {
            // Failure! No pressure sensor.
        }
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    @Override
    public void onDestroy()
    {
        if(counter != null)
        {
            SensorManager mgr = (SensorManager) getSystemService(this.SENSOR_SERVICE);
            mgr.unregisterListener(counter);
        }
    }
}
