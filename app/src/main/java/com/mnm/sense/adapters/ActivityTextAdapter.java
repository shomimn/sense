package com.mnm.sense.adapters;

import android.widget.TextView;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.ActivityRecognitionData;
import com.ubhave.sensormanager.data.pull.ActivityRecognitionDataList;

import java.util.ArrayList;

public class ActivityTextAdapter extends VisualizationAdapter<TextView, String>
{
    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        int count = data.size();

        return adaptOne(data.get(count - 1));
    }

    @Override
    public String adaptOne(SensorData data)
    {
        ActivityRecognitionDataList activities = (ActivityRecognitionDataList) data;
        String res = "";

        for(ActivityRecognitionData activity : activities.getActivities())
        {
            res += activity.getActivityText() + " : " + String.valueOf(activity.getConfidence());
        }

        return res;
    }

    @Override
    public ArrayList<String> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public void prepareView(TextView view)
    {

    }

    @Override
    public VisualizationAdapter<TextView, String> newInstance()
    {
        return new ActivityTextAdapter();
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        return null;
    }
}
