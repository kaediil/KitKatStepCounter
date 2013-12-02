package com.hodum.sensorreader.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.util.List;
import java.util.Vector;

public class SensorReadingsDB extends SQLiteOpenHelper {

    public static final String TABLE_STEPS = "steps";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_STEP_COUNT = "count";
    public static final String COLUMN_STEP_DATE = "date";
    public static final String COLUMN_STEP_DATE_TIME = "date_time";

    private static final String DATABASE_NAME = "steps.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_STEPS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_STEP_COUNT
            + " real not null, " + COLUMN_STEP_DATE + " text UNIQUE, "  +
            COLUMN_STEP_DATE_TIME + " INTEGER);";

    public SensorReadingsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SensorReadingsDB.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);
        onCreate(db);
    }

    public synchronized Pair<Double,Long> getValueForDate(String date)
    {

        Cursor res = getReadableDatabase().query(TABLE_STEPS,new String[]{COLUMN_STEP_COUNT,COLUMN_STEP_DATE_TIME}, COLUMN_STEP_DATE + "=?",
                new String[]{date}, null,null,null);
        Pair<Double,Long> retVal = null;// = new Pair<Double,Long>();
        while(res.moveToNext())
        {
             retVal = new Pair<Double, Long>(res.getDouble(0), res.getLong(1));

        }
        res.close();
        return retVal;
    }


    public synchronized Pair<Double,String> getMostRecentData()
    {

        Cursor res = getReadableDatabase().query(TABLE_STEPS,new String[]{COLUMN_STEP_COUNT,COLUMN_STEP_DATE}, null,
               null, null,null,COLUMN_STEP_DATE_TIME + " DESC");
        Pair<Double,String> retVal = null;// = new Pair<Double,Long>();
        if(res.moveToNext())
        {
            retVal = new Pair<Double, String>(res.getDouble(0), res.getString(1));

        }
        res.close();
        return retVal;
    }

    public synchronized List<Pair<Double,String>> getTwentyFiveMostRecentData()
    {

        List<Pair<Double,String>> retVal = new Vector<Pair<Double,String>>();
        Cursor res = getReadableDatabase().query(TABLE_STEPS,new String[]{COLUMN_STEP_COUNT,COLUMN_STEP_DATE}, null,
                null, null,null,COLUMN_STEP_DATE_TIME + " DESC");
        int count = 0;
        while(res.moveToNext() && count <25)
        {
            Pair<Double,String> item = null;// = new Pair<Double,Long>();

            item = new Pair<Double, String>(res.getDouble(0), res.getString(1));
            retVal.add(item);
            count++;
        }
        res.close();
        return retVal;
    }

    public synchronized long setValueForDate(String date, double value, long actualDate)
    {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STEP_COUNT,value);
        cv.put(COLUMN_STEP_DATE,date);
        cv.put(COLUMN_STEP_DATE_TIME,actualDate);
        return getWritableDatabase().insert(TABLE_STEPS,null,cv);
    }

    public synchronized long updateValueForDate(String date, double value, long actualDate)
    {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STEP_COUNT,value);
        cv.put(COLUMN_STEP_DATE,date);
        cv.put(COLUMN_STEP_DATE_TIME,actualDate);
        SQLiteDatabase db =getWritableDatabase();
        return db.update(TABLE_STEPS, cv , COLUMN_STEP_DATE + "=?",new String[] {date} );
    }
}
