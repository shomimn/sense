package com.mnm.sense.trackers;

import android.location.Location;
import android.util.Pair;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.mnm.sense.Locator;
import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.RunningApplicationBarAdapter;
import com.mnm.sense.adapters.RunningApplicationPieAdapter;
import com.mnm.sense.adapters.RunningApplicationTextAdapter;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.mnm.sense.models.TextModel;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.RunningApplicationData;
import com.ubhave.sensormanager.data.pull.RunningApplicationDataList;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class RunningApplicationTracker extends Tracker
{
    private static final String ATTRIBUTE_TOTAL = "Total time";

    public RunningApplicationTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_RUNNING_APP);

        text = "Running Apps";
        resource = R.drawable.ic_dashboard_black_48dp;
        isOn = false;

        accent = R.color.greenColorAccent;
        theme = R.style.GreenTheme;

        attributes = new String[]{ATTRIBUTE_TOTAL};

        build()
            .listView(new Visualization(2, 3, false))
            .barChart(new Visualization(2, 3, false))
            .pieChart(new Visualization(2, 3, false))
            .map(new Visualization(2, 3, false))
                .attribute(ATTRIBUTE_TOTAL)
                    .adapters(new RunningApplicationTextAdapter(),
                            new RunningApplicationBarAdapter(),
                            new RunningApplicationPieAdapter(),
                            new RunningAppsLatLngAdapter()
                    );
    }

    @Override
    protected void attachLocation(SensorData data)
    {
        Locator locator = Locator.instance();
        RunningApplicationDataList listData = (RunningApplicationDataList) data;

        for (RunningApplicationData appData : listData.getRunningApplications())
        {
            long timestamp = appData.getLastTimeUsed();
            Location location = locator.locateAt(timestamp);

            if (location == null)
                continue;

            appData.setLocation(Pair.create(location.getLatitude(), location.getLongitude()));
        }
    }
}

class RunningAppsLatLngAdapter extends VisualizationAdapter<GoogleMap, ArrayList<LatLng>>
{
    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        return adaptOne(data.get(data.size() - 1));
    }

    @Override
    public ArrayList<LatLng> adaptOne(SensorData data)
    {
        ArrayList<LatLng> result = new ArrayList<>();
        RunningApplicationDataList listData = (RunningApplicationDataList) data;

        for (RunningApplicationData appData : listData.getRunningApplications())
        {
            Pair<Double, Double> location = appData.getLocation();

            if (location != null)
                result.add(new LatLng(location.first, location.second));

        }

        return result;
    }

    @Override
    public ArrayList<ArrayList<LatLng>> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public VisualizationAdapter<GoogleMap, ArrayList<LatLng>> newInstance()
    {
        return null;
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        return null;
    }
}
