package com.mnm.sense.adapters;

import android.widget.TextView;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.RunningApplicationData;
import com.ubhave.sensormanager.data.pull.RunningApplicationDataList;

import java.util.ArrayList;

public class RunningApplicationTextAdapter implements VisualizationAdapter<TextView, String>
{

    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if(data.size() == 0)
            return null;

        int last = data.size() - 1;

        return adaptOne(data.get(last));
    }

    @Override
    public String adaptOne(SensorData data)
    {
        String result = "";
        RunningApplicationDataList appDataList = (RunningApplicationDataList)data;
        for(RunningApplicationData appData: appDataList.getRunningApplications())
        {
            result += "Name: " + appData.getName() + ", Foreground time: " + appData.getForegroundTime() + "\n";
        }
        return result;
    }

    @Override
    public ArrayList<String> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public void prepareView(TextView view)
    {

    }
}
