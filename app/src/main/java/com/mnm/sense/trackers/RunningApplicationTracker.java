package com.mnm.sense.trackers;

import android.location.Location;
import android.util.Pair;

import com.mnm.sense.adapters.RunningAppsLatLngAdapter;
import com.mnm.sense.Locator;
import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.RunningApplicationBarAdapter;
import com.mnm.sense.adapters.RunningApplicationPieAdapter;
import com.mnm.sense.adapters.RunningApplicationTextAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.RunningApplicationData;
import com.ubhave.sensormanager.data.pull.RunningApplicationDataList;
import com.ubhave.sensormanager.sensors.SensorUtils;

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
            .listView(new Visualization(2, 3))
            .barChart(new Visualization(2, 3))
            .pieChart(new Visualization(2, 3))
            .map(new Visualization(2, 3))
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

