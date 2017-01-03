package com.mnm.sense.trackers;

import com.mnm.sense.PermissionHandler;
import com.mnm.sense.R;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class ProximityTracker extends Tracker
{
    public ProximityTracker(PermissionHandler handler) throws ESException
    {
        super(handler, SensorUtils.SENSOR_TYPE_PROXIMITY);

        text = "Proximity";
        resource = R.drawable.ic_wifi_tethering_black_48dp;
        isOn = false;
    }
}

