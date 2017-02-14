package com.mnm.sense.adapters;

import com.ubhave.sensormanager.data.SensorData;

import java.util.ArrayList;

public abstract class VisualizationAdapter<T, U>
{
    public abstract Object adapt(ArrayList<SensorData> data);

    public abstract U adaptOne(SensorData data);
    public abstract ArrayList<U> adaptAll(ArrayList<SensorData> data);

    public void prepareView(T view)
    {
    }

    public abstract VisualizationAdapter<T, U> newInstance();

    public boolean isAggregating()
    {
        return false;
    }
}
