package com.mnm.sense.adapters;

import android.util.Pair;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.mnm.sense.R;
import com.mnm.sense.Timestamp;
import com.mnm.sense.map.AttributedPosition;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.RunningApplicationData;
import com.ubhave.sensormanager.data.pull.RunningApplicationDataList;

import java.util.ArrayList;

public class RunningAppsLatLngAdapter extends VisualizationAdapter<GoogleMap, ArrayList<AttributedPosition>>
{
    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        return adaptOne(data.get(data.size() - 1));
    }

    @Override
    public ArrayList<AttributedPosition> adaptOne(SensorData data)
    {
        ArrayList<AttributedPosition> result = new ArrayList<>();
        RunningApplicationDataList listData = (RunningApplicationDataList) data;

        for (RunningApplicationData appData : listData.getRunningApplications())
        {
            Pair<Double, Double> location = appData.getLocation();

            if (location != null)
            {
                LatLng latLng = new LatLng(location.first, location.second);
                String text = appData.getName();

                result.add(new AttributedPosition()
                        .origin(R.drawable.ic_dashboard_black_48dp)
                        .text("Running Apps")
                        .latLng(latLng)
                        .custom("Name:", appData.getName())
                        .custom("Date:", Timestamp.from(appData.getLastTimeUsed()).date())
                        .custom("Time:", Timestamp.from(appData.getLastTimeUsed()).time())
                        .custom("Foreground time:", String.valueOf(appData.getForegroundTimeMins()))
                );
            }

        }

        return result;
    }

    @Override
    public ArrayList<ArrayList<AttributedPosition>> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public VisualizationAdapter<GoogleMap, ArrayList<AttributedPosition>> newInstance()
    {
        return null;
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        return null;
    }
}
