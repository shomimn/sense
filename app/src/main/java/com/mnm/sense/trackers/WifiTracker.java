package com.mnm.sense.trackers;

import com.mnm.sense.R;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class WifiTracker extends Tracker
{
    public WifiTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_WIFI);

        text = "WiFi";
        resource = R.drawable.ic_wifi_black_48dp;
        isOn = false;
    }
}

