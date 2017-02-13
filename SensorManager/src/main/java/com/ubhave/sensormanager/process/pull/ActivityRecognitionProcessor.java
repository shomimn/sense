package com.ubhave.sensormanager.process.pull;

import android.content.Context;

import com.google.android.gms.location.DetectedActivity;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pull.ActivityRecognitionData;
import com.ubhave.sensormanager.data.pull.ActivityRecognitionDataList;
import com.ubhave.sensormanager.process.AbstractProcessor;

import java.util.List;

public class ActivityRecognitionProcessor extends AbstractProcessor
{

    public ActivityRecognitionProcessor(Context context, boolean rw, boolean sp)
    {
        super(context, rw, sp);
    }

    public ActivityRecognitionDataList process(long pullSenseStartTimestamp, List<DetectedActivity> detectedActivities, SensorConfig sensorConfig)
    {
        ActivityRecognitionDataList activities = new ActivityRecognitionDataList(pullSenseStartTimestamp, sensorConfig);

        for(DetectedActivity activity : detectedActivities)
        {
            int type = activity.getType();
            int conf = activity.getConfidence();
            long timestamp = System.currentTimeMillis();

            ActivityRecognitionData data = new ActivityRecognitionData(type, conf, timestamp);
            activities.addActivity(data);
        }

        return activities;
    }
}
