package com.hodum.sensorreader;

import android.app.Activity;

import android.app.Fragment;

import android.content.Intent;

import android.os.Bundle;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import com.hodum.sensorreader.db.SensorReadingsDB;

import java.util.List;

public class DisplayStepCount extends Activity {


    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_step_count);
        Intent i = new Intent(this, SensorService.class);
        this.startService(i);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


    }

    @Override
    public void onResume()
    {
        super.onResume();

        refreshDisplay();
    }

    private void refreshDisplay()
    {

        SensorReadingsDB db = new SensorReadingsDB(this);
        List<Pair<Double,String>> val = db.getTwentyFiveMostRecentData();
        LinearLayout parent = (LinearLayout)findViewById(R.id.viewLayout);
        parent.removeAllViews();
        if(val!=null && val.size()>0)
        {
            for(Pair<Double,String> item:val)
            {
                tv =(TextView) LayoutInflater.from(this).inflate(R.layout.item,null);
                tv.setText(item.second + " : " + item.first);
                parent.addView(tv);
            }
        }
        else
        {
            tv =(TextView) LayoutInflater.from(this).inflate(R.layout.item,null);
            tv.setText("No Values in DB");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_step_count, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            refreshDisplay();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_display_step_count, container, false);
            return rootView;
        }
    }

}
