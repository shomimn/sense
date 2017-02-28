package com.mnm.sense.adapters;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;
import android.util.SparseArray;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.mnm.sense.R;
import com.mnm.sense.Timestamp;
import com.mnm.sense.Util;
import com.mnm.sense.map.AttributedFeature;
import com.mnm.sense.map.Geometry;
import com.mnm.sense.map.Points;
import com.mnm.sense.map.SensePoint;
import com.mnm.sense.map.SensePolyline;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.ActivityRecognitionData;
import com.ubhave.sensormanager.data.pull.ActivityRecognitionDataList;
import com.ubhave.sensormanager.data.pull.RunningApplicationDataList;
import com.ubhave.sensormanager.data.push.CameraData;

import java.io.File;
import java.util.ArrayList;

public class ActivityLatLngAdapter extends VisualizationAdapter<GoogleMap, ArrayList<AttributedFeature>>
{
    private ActivityMonitor monitor;

    public ActivityLatLngAdapter(ActivityMonitor monitor)
    {
        this.monitor = monitor;
    }

    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        ArrayList<ArrayList<AttributedFeature>> result = adaptAll(data);

        if (result.size() == 0)
            return 0;
        return result.get(0);
    }

    @Override
    public ArrayList<AttributedFeature> adaptOne(SensorData data)
    {
        return null;
    }

    @Override
    public ArrayList<ArrayList<AttributedFeature>> adaptAll(ArrayList<SensorData> dataList)
    {
        ArrayList<AttributedFeature> lines = new ArrayList<>();

        ActivityMonitor.ActivityTimeTracker tracker = monitor.monitorPortion(dataList);
        Bitmap icon = Util.bitmapFromResource(R.drawable.ic_directions_run_black_48dp);

        for(ActivityMonitor.ActivityPath path : tracker.activityPaths)
        {

            lines.add(new AttributedFeature()
                    .origin(R.drawable.ic_directions_run_black_48dp)
                    .text(path.getText())
                    .icon(Util.bitmapFromResource(getResource(path.getType())))
                    .accent(getColor(path.getType()))
                    .geometry(SensePoint.make(path.getLocation()))
            );

            for(int i = 0; i < path.getPath().size() - 1; ++i)
            {
                LatLng start = path.getPath().get(i);
                LatLng end = path.getPath().get(i + 1);

                Points points = new Points();
                points.add(start);
                points.add(end);

                lines.add(new AttributedFeature()
                        .text("test")
                        .icon(icon)
                        .accent(getColor(path.getType()))
                        .geometry(SensePolyline.make(points))
                );
            }
        }

        ArrayList<ArrayList<AttributedFeature>> finalResult = new ArrayList<>();
        finalResult.add(lines);


        return finalResult;
    }

    private int getResource(int type)
    {
        switch (type)
        {
            case DetectedActivity.IN_VEHICLE:
                return R.drawable.ic_directions_car_black_48dp;
            case DetectedActivity.ON_BICYCLE:
                return R.drawable.ic_directions_bike_black_48dp;
            case DetectedActivity.WALKING:
                return R.drawable.ic_directions_walk_black_48dp;
            case DetectedActivity.RUNNING:
                return R.drawable.ic_directions_run_black_48dp;
            default:
                return R.drawable.ic_android_black_48dp;
        }
    }

    private int getColor(int type)
    {
        switch (type)
        {
            case DetectedActivity.IN_VEHICLE:
                return R.color.redColorAccent;
            case DetectedActivity.ON_BICYCLE:
                return R.color.greenColorAccent;
            case DetectedActivity.WALKING:
                return R.color.colorAccent;
            case DetectedActivity.RUNNING:
                return R.color.yellowColorAccent;
            default:
                return R.color.colorAccent;
        }
    }
    @Override
    public VisualizationAdapter<GoogleMap, ArrayList<AttributedFeature>> newInstance()
    {
        return new ActivityLatLngAdapter(monitor);
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        return null;
    }
}
