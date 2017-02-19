package com.mnm.sense.adapters;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.RunningApplicationData;
import com.ubhave.sensormanager.data.pull.RunningApplicationDataList;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class RunningApplicationAdapter<T,U> extends VisualizationAdapter<T, U>
{
    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public U adaptOne(SensorData data)
    {
        return null;
    }

    @Override
    public ArrayList<U> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public boolean isAggregating()
    {
        return true;
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        HashMap<String, ArrayList<SensorData>> dataByDay = partitionByDays(data);

        HashMap<String, RunningApplicationData> dataMap = new HashMap<>();

        if(data.size() == 0)
            return null;

        for(ArrayList<SensorData> dataList : dataByDay.values())
        {
            RunningApplicationDataList lastInDay = null;

            int i = dataList.size() - 1;
            while(lastInDay == null && i >= 0)
            {
                lastInDay = (RunningApplicationDataList)dataList.get(i--);
                if(lastInDay.getRunningApplications().size() == 0)
                    lastInDay = null;
            }

            if(lastInDay == null)
                return null;

            for(RunningApplicationData appData: lastInDay.getRunningApplications())
            {
                if(dataMap.containsKey(appData.getName()))
                {
                    RunningApplicationData adjustedData = dataMap.get(appData.getName());
                    adjustedData.setForegroundTime(adjustedData.getForegroundTime() + appData.getForegroundTime());
                    long ltu = appData.getLastTimeUsed() > adjustedData.getLastTimeUsed() ? appData.getLastTimeUsed() : adjustedData.getLastTimeUsed();
                    adjustedData.setLastTimeUsed(ltu);
                    dataMap.put(appData.getName(), adjustedData);
                }
                else
                    dataMap.put(appData.getName(), appData);
            }
        }

        RunningApplicationDataList result = new RunningApplicationDataList(0, data.get(0).getSensorConfig());
        result.setRunningApplications(new ArrayList<>(dataMap.values()));
        return adaptOne(result);
    }
}
