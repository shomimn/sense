package com.ubhave.sensormanager.data.pull;


import android.util.Pair;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.ArrayList;

public class ActivityRecognitionDataList extends SensorData
{
    private ArrayList<ActivityRecognitionData> activities;
    private Pair<Double, Double> location;

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

    public Pair<Double, Double> getLocation()
    {
        return location;
    }

    public void setLocation(Pair<Double, Double> location)
    {
        this.location = location;
    }
}
