package com.mnm.sense.trackers;

import android.os.Looper;
import android.widget.TextView;

import com.mnm.sense.NotificationCreator;
import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.push.ScreenData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class ScreenTracker extends Tracker
{
    public static final String ATTRIBUTE_TIME = "Time";

    public long prevTimestamp = 0;
    public long timeOn = 0;
    public long timeOff = 0;

    public ScreenTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_SCREEN);

        text = "Screen";
        resource = R.drawable.ic_screen_lock_landscape_black_48dp;
        isOn = false;

        attributes = new String[] { ATTRIBUTE_TIME };

        visualizations.put(Visualization.TEXT, new Visualization(1, 1, false));

        limit = new Limit("Daily limit", 4, 1, 12);

//        accent = android.R.color.black;

        HashMap<String, VisualizationAdapter> timeAdapters = new HashMap<>();
        timeAdapters.put(Visualization.TEXT, new ScreenTextAdapter());

        adapters.put(ATTRIBUTE_TIME, timeAdapters);
    }

    @Override
    public void correctData(SensorData data)
    {
        ScreenData screenData = (ScreenData) data;
        long timestamp = screenData.getTimestamp();
        long diff = timestamp - prevTimestamp;

        if (screenData.isOn())
            timeOff += diff;
        else
            timeOn += diff;

        prevTimestamp = timestamp;
    }

    @Override
    public void limitNotification(SensorData data)
    {
        long timeOnSeconds = timeOn / 1000;
//        long limitSeconds = limit.value * 60 * 60;
        long limitSeconds = 60;

        if (timeOnSeconds > limitSeconds)
        {
            NotificationCreator.create(resource, "Sense", "Whoa, you've been using your phone too much! You should take a walk!");
        }
    }

    @Override
    public void start() throws ESException
    {
        prevTimestamp = System.currentTimeMillis();

        super.start();
    }
}

class ScreenTextAdapter implements VisualizationAdapter<TextView, String>
{
    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        int size = data.size();

        if (size == 0)
            return null;

        return adaptOne(data.get(size - 1));
    }

    @Override
    public String adaptOne(SensorData data)
    {
        ScreenData screenData = (ScreenData) data;

        return String.valueOf(screenData.isOn());
    }

    @Override
    public ArrayList<String> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public void prepareView(TextView view)
    {

    }
}

