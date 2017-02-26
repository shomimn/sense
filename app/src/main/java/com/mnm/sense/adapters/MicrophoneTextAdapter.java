package com.mnm.sense.adapters;


import android.widget.TextView;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.MicrophoneData;

import java.util.ArrayList;

public class MicrophoneTextAdapter extends VisualizationAdapter<TextView, String>
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
        MicrophoneData micData = (MicrophoneData) data;
        String res = "";

        for(double decibel : micData.getDecibelsArray())
        {
            res += String.valueOf(decibel) + "\t";
        }
        return res;
    }

    @Override
    public ArrayList<String> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public VisualizationAdapter<TextView, String> newInstance()
    {
        return null;
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        return null;
    }
}
