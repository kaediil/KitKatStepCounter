package com.hodum.sensorreader;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;
import com.hodum.sensorreader.db.SensorReadingsDB;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by fhodum on 11/12/13.
 */
public class StepCountListener implements SensorEventListener
{

    private Context ctxt;

    public StepCountListener(Context context)
    {
        ctxt = context;

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("SENSORREADER",event.sensor.getName() + " :  " + event.timestamp);

        long rebootTime = SystemClock.elapsedRealtime();
        long timeNow = System.currentTimeMillis();
        long whenRebooted =  timeNow - rebootTime;
        SensorReadingsDB db = new SensorReadingsDB(ctxt);
        Calendar cal = Calendar.getInstance();
        String date  = DateFormat.format("M/d/y",cal).toString();

        Pair<Double,Long> values = db.getValueForDate(date);
        double updated = event.values[0];

        if(values!=null)
        {
            if(DateUtils.isToday(whenRebooted))
            {
                //query the date, add the item
                if(values.second < whenRebooted)
                {
                    Log.d("SNEORREADER******",values.first + " : " +values.second + " : " + updated );
                    updated += values.first;
                }
            }
            db.updateValueForDate(date,updated,timeNow);
        }
        else
        {
            db.setValueForDate(date,event.values[0],timeNow);
        }
        //mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
