package com.mnm.sense.trackers;

import com.mnm.sense.R;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.env.LightData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class LightTracker extends Tracker
{
    public LightTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_LIGHT);

        text = "Light";
        resource = R.drawable.ic_brightness_medium_black_48dp;
        isOn = false;
    }
}
