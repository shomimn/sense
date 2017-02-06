package com.mnm.sense.trackers;

import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.push.ScreenData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class ScreenTracker extends Tracker
{
    public ScreenTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_SCREEN);

        text = "Screen";
        resource = R.drawable.ic_screen_lock_landscape_black_48dp;
        isOn = false;

        visualizations.put(Visualization.TEXT, new Visualization(1, 1, false));

        limit = new Limit("Daily limit", 4, 1, 12);

//        accent = android.R.color.black;
    }
}

