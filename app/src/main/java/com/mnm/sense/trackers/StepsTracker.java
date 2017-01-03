package com.mnm.sense.trackers;

import com.mnm.sense.PermissionHandler;
import com.mnm.sense.R;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class StepsTracker extends Tracker
{
    public StepsTracker(PermissionHandler handler) throws ESException
    {
        super(handler, SensorUtils.SENSOR_TYPE_STEP_COUNTER);

        text = "Steps";
        resource = R.drawable.ic_directions_walk;
        isOn = false;
    }
}

