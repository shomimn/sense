package com.mnm.sense.trackers;

import com.mnm.sense.R;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.pull.BluetoothData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class BluetoothTracker extends Tracker
{
    public BluetoothTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_BLUETOOTH);

        text = "Bluetooth";
        resource = R.drawable.ic_bluetooth_black_48dp;
        isOn = false;
    }
}
