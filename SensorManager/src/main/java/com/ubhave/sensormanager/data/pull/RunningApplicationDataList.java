package com.ubhave.sensormanager.data.pull;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.ArrayList;


public class RunningApplicationDataList extends SensorData
{
    private ArrayList<RunningApplicationData> runningApplications;
    public RunningApplicationDataList(long sensorTimestamp, SensorConfig config)
    {
        super(sensorTimestamp, config);
        runningApplications = new ArrayList<>();
    }

    public void setRunningApplications(ArrayList<RunningApplicationData> runningApplications)
    {
        this.runningApplications = runningApplications;
    }

    public void addRunningApplication(RunningApplicationData appData)
    {
        runningApplications.add(appData);
    }

    public ArrayList<RunningApplicationData> getRunningApplications()
    {
        return this.runningApplications;
    }
    @Override
    public int getSensorType()
    {
        return SensorUtils.SENSOR_TYPE_RUNNING_APP;
    }
}
