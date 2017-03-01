package com.mnm.sense.adapters;

import android.hardware.Sensor;
import android.util.Pair;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.mnm.sense.R;
import com.mnm.sense.Timestamp;
import com.mnm.sense.Util;
import com.mnm.sense.map.AttributedFeature;
import com.mnm.sense.map.SensePoint;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.RunningApplicationData;
import com.ubhave.sensormanager.data.pull.RunningApplicationDataList;

import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class RunningAppsLatLngAdapter extends VisualizationAdapter<GoogleMap, ArrayList<AttributedFeature>>
{
    @Override
    public ArrayList<AttributedFeature> adapt(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        return adaptAll(data).get(0);
//        return adaptOne(data.get(data.size() - 1));
    }

    @Override
    public ArrayList<AttributedFeature> adaptOne(SensorData data)
    {
        ArrayList<AttributedFeature> result = new ArrayList<>();
        RunningApplicationDataList listData = (RunningApplicationDataList) data;

        for (RunningApplicationData appData : listData.getRunningApplications())
        {
            Pair<Double, Double> location = appData.getLocation();

            if (location != null)
            {
                LatLng latLng = new LatLng(location.first, location.second);
                String text = appData.getName();

                result.add(new AttributedFeature()
                        .origin(R.drawable.ic_dashboard_black_48dp)
                        .icon(Util.drawableToBitmap(appData.getIcon()))
                        .text("Running Apps")
                        .geometry(SensePoint.make(latLng))
                        .custom("Name:", appData.getName())
                        .custom("Date:", Timestamp.from(appData.getLastTimeUsed()).date())
                        .custom("Time:", Timestamp.from(appData.getLastTimeUsed()).time())
                        .custom("Foreground time:", String.valueOf(appData.getForegroundTimeMins()))
                );
            }
        }

        return result;
    }

    public ArrayList<AttributedFeature> adaptUnvisited(SensorData data, HashMap<String, HashSet<Long>> visited)
    {
        ArrayList<AttributedFeature> result = new ArrayList<>();
        RunningApplicationDataList listData = (RunningApplicationDataList) data;

        for (RunningApplicationData appData : listData.getRunningApplications())
        {
            Pair<Double, Double> location = appData.getLocation();

            if (location != null)
            {
                long lastTimeUsed = appData.getLastTimeUsed();
                String packageName = appData.getPackageName();

                if (visited.get(packageName) == null)
                    visited.put(packageName, new HashSet<Long>());

                if (visited.get(packageName).contains(lastTimeUsed))
                    continue;

                LatLng latLng = new LatLng(location.first, location.second);

                result.add(new AttributedFeature()
                        .origin(R.drawable.ic_dashboard_black_48dp)
                        .icon(Util.drawableToBitmap(appData.getIcon()))
                        .text("Running Apps")
                        .geometry(SensePoint.make(latLng))
                        .custom("Name:", appData.getName())
                        .custom("Date:", Timestamp.from(lastTimeUsed).date())
                        .custom("Time:", Timestamp.from(lastTimeUsed).time())
                        .custom("Foreground time:", String.valueOf(appData.getForegroundTimeMins()))
                );

                visited.get(packageName).add(lastTimeUsed);
            }
        }

        return result;
    }

    @Override
    public ArrayList<ArrayList<AttributedFeature>> adaptAll(ArrayList<SensorData> data)
    {
        ArrayList<ArrayList<AttributedFeature>> result = new ArrayList<>();
        ArrayList<AttributedFeature> features = new ArrayList<>();
        HashMap<String, HashSet<Long>> visited = new HashMap<>();

        for (SensorData sensorData : data)
            features.addAll(adaptUnvisited(sensorData, visited));

        result.add(features);

        return result;
    }

    @Override
    public RunningAppsLatLngAdapter newInstance()
    {
        return new RunningAppsLatLngAdapter();
    }

    @Override
    public boolean isAggregating()
    {
        return true;
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        HashMap<String, ArrayList<SensorData>> dataByDays = partitionByDays(data);
        ArrayList<AttributedFeature> features = new ArrayList<>();

        for (ArrayList<SensorData> dailyData : dataByDays.values())
            features.addAll(adapt(dailyData));

        return features;
    }
}
