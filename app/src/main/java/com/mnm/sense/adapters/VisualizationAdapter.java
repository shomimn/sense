package com.mnm.sense.adapters;

import com.ubhave.sensormanager.data.SensorData;

import java.util.ArrayList;

public interface VisualizationAdapter<T, U>
{
    Object adapt(ArrayList<SensorData> data);

    U adaptOne(SensorData data);
    ArrayList<U> adaptAll(ArrayList<SensorData> data);

    void prepareView(T view);
}
