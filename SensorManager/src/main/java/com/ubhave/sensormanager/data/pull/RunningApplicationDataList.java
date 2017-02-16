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
    public static final int TIME_THRESHOLD_MINS = 100;
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

    @TargetApi(21)
    public ArrayList<RunningApplicationData> getMostUsedApplications()
    {
        ArrayList<RunningApplicationData> result = new ArrayList<>();
        long otherForegroundTime = 0;
        for(RunningApplicationData data : runningApplications)
        {
            if(data.getForegroundTimeMins() >= TIME_THRESHOLD_MINS)
                result.add(data);
            else
                otherForegroundTime += data.getForegroundTime();
        }

        Drawable icon = ESSensorManager.getSensorManager().getApplicationContext().getDrawable(R.drawable.ic_group_work_black_48dp);
        RunningApplicationData other = new RunningApplicationData("Other", otherForegroundTime, icon, 0, 0, 0);

        result.add(other);
        return result;
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
