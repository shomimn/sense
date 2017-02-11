package com.mnm.sense.adapters;

import android.widget.TextView;

import com.mnm.sense.adapters.VisualizationAdapter;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.StepCounterData;

import java.util.ArrayList;

public class StepsTextAdapter implements VisualizationAdapter<TextView, String>
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
}
