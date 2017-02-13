package com.ubhave.sensormanager.data.pull;


import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.ArrayList;

public class ActivityRecognitionDataList extends SensorData
{
    private ArrayList<ActivityRecognitionData> activities;

    public ActivityRecognitionDataList(long sensorTimestamp, SensorConfig config)
    {
        super(sensorTimestamp, config);
        activities = new ArrayList<>();
    }

    @Override
    public int getSensorType()
    {
        return SensorUtils.SENSOR_TYPE_ACTIVITY_RECOGNITION;
    }

    public ArrayList<ActivityRecognitionData> getActivities()
    {
        return activities;
    }

    public void setActivities(ArrayList<ActivityRecognitionData> activities)
    {
        this.activities = activities;
    }

    public void addActivity(ActivityRecognitionData data)
    {
        this.activities.add(data);
    }
}
