package com.mnm.sense.adapters;

import android.widget.TextView;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.push.ScreenData;

import java.util.ArrayList;

public class ScreenTextAdapter extends VisualizationAdapter<TextView, String>
{
    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        int size = data.size();

        if (size == 0)
            return null;

        return adaptOne(data.get(size - 1));
    }

    @Override
    public String adaptOne(SensorData data)
    {
        ScreenData screenData = (ScreenData) data;
        String result = "";

        result += "Time on: " + String.valueOf(screenData.getTimeOn() / 1000) + "\n"
               + "Time off: " + String.valueOf(screenData.getTimeOff() / 1000);

        return result;
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
        return new ScreenTextAdapter();
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        return null;
    }
}
