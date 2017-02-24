package com.mnm.sense.trackers;

import android.content.SharedPreferences;
import android.util.Log;

import com.mnm.sense.NotificationCreator;
import com.mnm.sense.R;
import com.mnm.sense.Timestamp;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.ScreenPieAdapter;
import com.mnm.sense.adapters.ScreenTextAdapter;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.push.ScreenData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.HashMap;

public class ScreenTracker extends Tracker
{
    public static final String ATTRIBUTE_TIME = "Time";
    public static final long GUARD_SLEEP_INTERVAL = 60 * 60 * 1000;

    public boolean screenOn = true;
    public long prevTimestamp = 0;
    public long timeOn = 0;
    public long timeOff = 0;

    Runnable limitGuard = new Runnable()
    {
        @Override
        public void run()
        {
            Log.d("limitGuard", "Checking limits");

            long diff = System.currentTimeMillis() - prevTimestamp;

            if (screenOn)
            {
                long time = timeOn + diff;
                checkLimit(time);
            }

            handler.postDelayed(this, GUARD_SLEEP_INTERVAL);
        }
    };

    public ScreenTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_SCREEN);

        text = "Screen";
        resource = R.drawable.ic_screen_lock_landscape_black_48dp;
        isOn = false;

        attributes = new String[] { ATTRIBUTE_TIME };

        limit = new Limit("Daily limit", 4, 1, 12);

        accent = R.color.redColorAccent;
        theme = R.style.RedTheme;

        build()
            .text(new Visualization(1, 1, false))
            .pieChart(new Visualization(1, 3, false))
            .attribute(ATTRIBUTE_TIME)
            .adapters(new ScreenTextAdapter(),
                    new ScreenPieAdapter());
    }

    @Override
    public void correctData(SensorData data)
    {
        ScreenData screenData = (ScreenData) data;
        long timestamp = screenData.getTimestamp();
        long diff = timestamp - prevTimestamp;
        screenOn = screenData.isOn();

        if (screenOn)
            timeOff += diff;
        else
            timeOn += diff;

        screenData.setTimeOff(timeOff);
        screenData.setTimeOn(timeOn);

        prevTimestamp = timestamp;
    }

    @Override
    public void limitNotification(SensorData data)
    {
        checkLimit(timeOn);
    }

    public void checkLimit(long time)
    {
        SharedPreferences prefs = getConfig();

        String date = Timestamp.now().date();

        if (prefs.getBoolean(date, false))
            return;

        long timeOnSeconds = time / 1000;
        long limitSeconds = limit.value * 60 * 60;

        if (timeOnSeconds >= limitSeconds)
        {
            NotificationCreator.create(resource, "Sense", "Whoa, you've been using your phone too much! You should take a walk!");

            prefs.edit().putBoolean(date, true).commit();
        }
    }

    @Override
    public void start() throws ESException
    {
        prevTimestamp = System.currentTimeMillis();

        handler.postDelayed(limitGuard, GUARD_SLEEP_INTERVAL);

        super.start();
    }

    @Override
    public void pause() throws ESException
    {
        super.pause();

        handler.removeCallbacks(limitGuard);
    }

    @Override
    public void unpause() throws ESException
    {
        super.unpause();

        handler.postDelayed(limitGuard, GUARD_SLEEP_INTERVAL);
    }

    @Override
    public void purge()
    {
        prevTimestamp = 0;
        timeOn = 0;
        timeOff = 0;

        super.purge();
    }
}

