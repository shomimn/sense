package com.mnm.sense.trackers;

import com.mnm.sense.PermissionHandler;
import com.mnm.sense.R;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class BluetoothTracker extends Tracker
{
    public BluetoothTracker(PermissionHandler handler) throws ESException
    {
        super(handler, SensorUtils.SENSOR_TYPE_BLUETOOTH);

        text = "Bluetooth";
        resource = R.drawable.ic_bluetooth_black_48dp;
        isOn = false;
    }
}
