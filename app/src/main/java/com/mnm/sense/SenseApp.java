package com.mnm.sense;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.mnm.sense.trackers.ActivityTracker;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static java.security.AccessController.getContext;

public class SenseApp extends Application
{
    public static final String PREFS_KEY = "com.mnm.sense.appPrefs";
    public static final String INSTALLED_DATE_KEY = "installedDate";

    private static SenseApp instance_;
    public LinkedHashMap<Integer, Tracker> trackers = new LinkedHashMap<>();
    private static Handler handler = new Handler();

    Runnable purgeTask = new Runnable()
    {
        @Override
        public void run()
        {
            for (Tracker tracker : trackers.values())
                tracker.purge();

            schedulePurging();
        }
    };

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance_ = this;

        try
        {
            trackers.put(SensorUtils.SENSOR_TYPE_STEP_COUNTER, new StepsTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_ACTIVITY_RECOGNITION, new ActivityTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_LOCATION, new LocationTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_SMS_CONTENT_READER, new SMSContentTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_CALL_CONTENT_READER, new CallLogTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_RUNNING_APP, new RunningApplicationTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_BATTERY, new BatteryTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_SCREEN, new ScreenTracker());
            trackers.put(SensorUtils.SENSOR_TYPE_WIFI, new WifiTracker());
//            trackers.put(SensorUtils.SENSOR_TYPE_BLUETOOTH, new BluetoothTracker());
//            trackers.put(SensorUtils.SENSOR_TYPE_LIGHT, new LightTracker());
//            trackers.put(SensorUtils.SENSOR_TYPE_PROXIMITY, new ProximityTracker());
        }
        catch (ESException e)
        {
            e.printStackTrace();
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);

        if (!prefs.contains(INSTALLED_DATE_KEY))
            prefs.edit().putLong(INSTALLED_DATE_KEY, System.currentTimeMillis()).commit();

        setTrackerDefaults();

        schedulePurging();
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

    private void setTrackerDefaults()
    {
        for (Tracker tracker : trackers.values())
        {
            SharedPreferences prefs = tracker.getConfig();

            if (prefs.contains(Tracker.DEFAULT_VISUALIZATIONS_KEY))
                continue;

            Set<String> defaultVisualizations = new HashSet<>();

            for (String visualization : tracker.visualizations.keySet())
                defaultVisualizations.add(visualization);

            prefs.edit().putStringSet(Tracker.DEFAULT_VISUALIZATIONS_KEY, defaultVisualizations).commit();
        }
    }

    public static String deviceId()
    {
        return Settings.Secure.getString(context().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    private void schedulePurging()
    {
        Calendar cal = Calendar.getInstance();

        long now = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
//        cal.add(Calendar.MINUTE, 2);
        long then = cal.getTimeInMillis();

        Log.d("Purge", "Purging data at " + cal.getTime().toString());

        handler.postDelayed(purgeTask, then - now);
    }
}
