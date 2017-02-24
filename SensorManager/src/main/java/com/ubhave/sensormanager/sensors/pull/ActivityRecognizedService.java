package com.ubhave.sensormanager.sensors.pull;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;

public class ActivityRecognizedService extends IntentService
{
    ActivityRecognitionSensor sensor;

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
        sensor = ActivityRecognitionSensor.getSensor();
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (sensor == null)
        {
            stopSelf();
            return;
        }

        if(ActivityRecognitionResult.hasResult(intent)) {
            Log.d("ARS", "onHandleIntent");
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            sensor.handleDetectedActivities( result.getProbableActivities() );
        }
    }
}
