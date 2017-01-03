package com.mnm.sense.trackers;

import com.mnm.sense.PermissionHandler;
import com.mnm.sense.R;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class PassiveLocationTracker extends Tracker
{
    public PassiveLocationTracker(PermissionHandler handler) throws ESException
    {
        super(handler, SensorUtils.SENSOR_TYPE_PASSIVE_LOCATION);

        text = "Location";
        resource = R.drawable.ic_my_location_black_48dp;
        isOn = false;
    }
}
