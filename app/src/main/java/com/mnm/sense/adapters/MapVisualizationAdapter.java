package com.mnm.sense.adapters;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.ubhave.sensormanager.data.SensorData;

import java.util.ArrayList;

public class MapVisualizationAdapter implements VisualizationAdapter<GoogleMap, LatLng>
{
    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public LatLng adaptOne(SensorData data)
    {
        return null;
    }

    @Override
    public ArrayList<LatLng> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public void prepareView(GoogleMap view)
    {

    }
}
