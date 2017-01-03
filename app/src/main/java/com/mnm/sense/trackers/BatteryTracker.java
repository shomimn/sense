package com.mnm.sense.trackers;


import android.hardware.Sensor;

import com.mnm.sense.PermissionHandler;
import com.mnm.sense.R;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class BatteryTracker extends Tracker
{
    public BatteryTracker(PermissionHandler handler) throws ESException
    {
        super(handler, SensorUtils.SENSOR_TYPE_BATTERY);

        text = "Battery";
        resource = R.drawable.ic_battery_full_black_48dp;
        isOn = false;
    }
}

