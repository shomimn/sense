package com.ubhave.sensormanager.data.pull;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.R;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class RunningApplicationDataList extends SensorData
{
    private ArrayList<RunningApplicationData> runningApplications;
    private long totalForegroundTime;

    public RunningApplicationDataList(long sensorTimestamp, SensorConfig config)
    {
        super(sensorTimestamp, config);
        runningApplications = new ArrayList<>();
        totalForegroundTime = 0;
    }

    public void setRunningApplications(ArrayList<RunningApplicationData> runningApplications)
    {
        this.runningApplications = runningApplications;
    }

    public void addRunningApplication(RunningApplicationData appData)
    {
        runningApplications.add(appData);
        totalForegroundTime += appData.getForegroundTime();
    }

    public ArrayList<RunningApplicationData> getRunningApplications()
    {
        return this.runningApplications;
    }

    public ArrayList<RunningApplicationData> sort()
    {
        Collections.sort(runningApplications, new Comparator<RunningApplicationData>()
        {
            @Override
            public int compare(RunningApplicationData t1, RunningApplicationData t2)
            {
                return (int)(t2.getForegroundTime() - t1.getForegroundTime());
            }
        });
        return runningApplications;
    }

    @Override
    public int getSensorType()
    {
        return SensorUtils.SENSOR_TYPE_RUNNING_APP;
    }

    public long getTotalForegroundTime()
    {
        return totalForegroundTime;
    }

    public RunningApplicationData getDataAt(int i)
    {
        return runningApplications.get(i);
    }
}
