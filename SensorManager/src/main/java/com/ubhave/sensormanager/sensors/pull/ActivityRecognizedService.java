package com.ubhave.sensormanager.sensors.pull;

import android.app.IntentService;
import android.content.Intent;

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
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            sensor.handleDetectedActivities( result.getProbableActivities() );
        }
    }
}
