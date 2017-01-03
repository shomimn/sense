package com.mnm.sense.trackers;

import com.mnm.sense.PermissionHandler;
import com.mnm.sense.R;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class WifiTracker extends Tracker
{
    public WifiTracker(PermissionHandler handler) throws ESException
    {
        super(handler, SensorUtils.SENSOR_TYPE_WIFI);

        text = "WiFi";
        resource = R.drawable.ic_wifi_black_48dp;
        isOn = false;
    }
}

