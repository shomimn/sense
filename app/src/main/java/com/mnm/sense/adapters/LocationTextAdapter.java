package com.mnm.sense.adapters;

import android.location.Location;
import android.widget.TextView;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.LocationData;

import java.util.ArrayList;

public class LocationTextAdapter extends VisualizationAdapter<TextView, String>
{
    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        int last = data.size() - 1;

        return adaptOne(data.get(last));
    }

    @Override
    public String adaptOne(SensorData data)
    {
        LocationData locationData = (LocationData) data;
        Location location = locationData.getLastLocation();

        return "(" + String.valueOf(location.getLatitude()) + ", " + String.valueOf(location.getLongitude()) + ")";
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
        return new LocationTextAdapter();
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        return null;
    }
}
