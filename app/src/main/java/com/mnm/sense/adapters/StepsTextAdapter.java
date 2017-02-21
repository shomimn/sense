package com.mnm.sense.adapters;

import android.widget.TextView;

import com.mnm.sense.adapters.VisualizationAdapter;
import com.mnm.sense.trackers.Tracker;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.StepCounterData;

import java.util.ArrayList;

public class StepsTextAdapter extends VisualizationAdapter<TextView, String>
{
    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        int sum = 0;

        for (SensorData sensorData : data)
            sum += ((StepCounterData) sensorData).getNumSteps();

        return String.valueOf(sum);
    }

    @Override
    public String adaptOne(SensorData data)
    {
        StepCounterData stepsData = (StepCounterData) data;

        return String.valueOf((int)stepsData.getNumSteps());
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
        return new StepsTextAdapter();
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        return null;
    }
}
