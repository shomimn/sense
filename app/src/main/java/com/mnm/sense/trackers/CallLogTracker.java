package com.mnm.sense.trackers;

import android.Manifest;

import com.mnm.sense.PermissionHandler;
import com.mnm.sense.R;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class CallLogTracker extends Tracker
{
    public CallLogTracker(PermissionHandler handler) throws ESException
    {
        super(handler, SensorUtils.SENSOR_TYPE_CALL_CONTENT_READER);

        text = "Calls";
        resource = R.drawable.ic_phone_in_talk_black_48dp;
        isOn = false;

        permissions = new String[]{
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_CALL_LOG
        };

//        requestPermission();
    }
}

