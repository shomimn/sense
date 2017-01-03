package com.mnm.sense.trackers;

import com.mnm.sense.PermissionHandler;
import com.mnm.sense.R;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class LightTracker extends Tracker
{
    public LightTracker(PermissionHandler handler) throws ESException
    {
        super(handler, SensorUtils.SENSOR_TYPE_LIGHT);

        text = "Light";
        resource = R.drawable.ic_brightness_medium_black_48dp;
        isOn = false;
    }
}
