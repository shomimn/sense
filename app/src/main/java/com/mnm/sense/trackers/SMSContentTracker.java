package com.mnm.sense.trackers;

import com.mnm.sense.PermissionHandler;
import com.mnm.sense.R;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class SMSContentTracker extends Tracker
{
    public SMSContentTracker(PermissionHandler handler) throws ESException
    {
        super(handler, SensorUtils.SENSOR_TYPE_SMS_CONTENT_READER);

        text = "SMS";
        resource = R.drawable.ic_sms_black_48dp;
        isOn = false;

        permissions = new String[]{
                android.Manifest.permission.READ_SMS,
                android.Manifest.permission.RECEIVE_SMS
        };

//        requestPermission();
    }

//    @Override
//    public void start() throws ESException
//    {
//        requestPermission();
//    }
}

