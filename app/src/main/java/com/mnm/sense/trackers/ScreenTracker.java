package com.mnm.sense.trackers;

import com.mnm.sense.PermissionHandler;
import com.mnm.sense.R;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class ScreenTracker extends Tracker
{
    public ScreenTracker(PermissionHandler handler) throws ESException
    {
        super(handler, SensorUtils.SENSOR_TYPE_SCREEN);

        text = "Screen";
        resource = R.drawable.ic_screen_lock_landscape_black_48dp;
        isOn = false;
    }
}

