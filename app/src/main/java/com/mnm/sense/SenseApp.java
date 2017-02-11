package com.mnm.sense;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.mnm.sense.trackers.BatteryTracker;
import com.mnm.sense.trackers.BluetoothTracker;
import com.mnm.sense.trackers.CallLogTracker;
import com.mnm.sense.trackers.LightTracker;
import com.mnm.sense.trackers.LocationTracker;
import com.mnm.sense.trackers.ProximityTracker;
import com.mnm.sense.trackers.RunningApplicationTracker;
import com.mnm.sense.trackers.SMSContentTracker;
import com.mnm.sense.trackers.ScreenTracker;
import com.mnm.sense.trackers.StepsTracker;
import com.mnm.sense.trackers.Tracker;
import com.mnm.sense.trackers.WifiTracker;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class SenseApp extends Application
{
    public static final String PREFS_KEY = "com.mnm.sense.appPrefs";
    public static final String INSTALLED_DATE_KEY = "installedDate";

    private static SenseApp instance_;
    public HashMap<Integer, Tracker> trackers = new HashMap<>();

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance_ = this;

        try
        {
            trackers.put(SensorUtils.SENSOR_TYPE_STEP_COUNTER, new StepsTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_WIFI, new WifiTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_BATTERY, new BatteryTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_LOCATION, new LocationTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_SMS_CONTENT_READER, new SMSContentTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_BLUETOOTH, new BluetoothTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_CALL_CONTENT_READER, new CallLogTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_LIGHT, new LightTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_PROXIMITY, new ProximityTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_SCREEN, new ScreenTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_RUNNING_APP, new RunningApplicationTracker());
        }
        catch (ESException e)
        {
            e.printStackTrace();
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);

        if (!prefs.contains(INSTALLED_DATE_KEY))
            prefs.edit().putLong(INSTALLED_DATE_KEY, System.currentTimeMillis()).commit();
    }

    public static SenseApp instance()
    {
        return instance_;
    }

    public static Context context()
    {
        return instance_.getApplicationContext();
    }

    public Tracker tracker(int type)
    {
        return trackers.get(type);
    }

    public long installedDate()
    {
        return getSharedPreferences(PREFS_KEY, MODE_PRIVATE).getLong(INSTALLED_DATE_KEY, System.currentTimeMillis());
    }
}
