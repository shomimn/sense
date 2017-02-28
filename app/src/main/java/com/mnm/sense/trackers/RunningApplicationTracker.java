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

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;

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
            .map(new Visualization(0, 0))
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

    @Override
    protected boolean isNewData(SensorData data)
    {
        if (sensorData.size() == 0)
            return true;

        RunningApplicationDataList prevList = (RunningApplicationDataList) sensorData.get(sensorData.size() - 1);
        RunningApplicationDataList newList = (RunningApplicationDataList) data;

        if (prevList.getRunningApplications().size() != newList.getRunningApplications().size())
            return true;

        Comparator<RunningApplicationData> comparator = new Comparator<RunningApplicationData>()
        {
            @Override
            public int compare(RunningApplicationData d1, RunningApplicationData d2)
            {
                return d1.getName().compareTo(d2.getName());
            }
        };

        Collections.sort(prevList.getRunningApplications(), comparator);
        Collections.sort(newList.getRunningApplications(), comparator);

        Field[] fields = RunningApplicationData.class.getDeclaredFields();

        for (int i = 0; i < prevList.getRunningApplications().size(); ++i)
        {
            RunningApplicationData prevData = prevList.getDataAt(i);
            RunningApplicationData newData = newList.getDataAt(i);

            for (Field field : fields)
            {
                if (field.getName().equals("icon") || field.getName().equals("location"))
                    continue;

                try
                {
                    if (!field.isAccessible())
                        field.setAccessible(true);

                    Object prevValue = field.get(prevData);
                    Object newValue = field.get(newData);

                    if (!prevValue.equals(newValue))
                        return true;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }
}

