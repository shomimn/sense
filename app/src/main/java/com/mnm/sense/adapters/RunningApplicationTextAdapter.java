package com.mnm.sense.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mnm.sense.R;
import com.mnm.sense.initializers.ListViewInitializer;
import com.mnm.sense.models.ListViewData;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.RunningApplicationData;
import com.ubhave.sensormanager.data.pull.RunningApplicationDataList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RunningApplicationTextAdapter extends VisualizationAdapter<ListView, ListViewData>
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
    public ListViewData adaptOne(SensorData data)
    {
        RunningApplicationDataList appDataList = (RunningApplicationDataList)data;

        int size = appDataList.getRunningApplications().size();
        Drawable[] images = new Drawable[size];
        String[] names = new String[size];
        long[] lastTimeUsages = new long[size];
        float[] foregroundTimes = new float[size];

        int i = 0;
        for(RunningApplicationData appData: appDataList.getRunningApplications())
        {
            images[i] = appData.getIcon();
            names[i] = appData.getName();
            lastTimeUsages[i] = appData.getLastTimeUsed();
            foregroundTimes[i++] = appData.getForegroundTimeMins();
        }
        return new ListViewData(names, images, foregroundTimes, lastTimeUsages);
    }

    @Override
    public ArrayList<ListViewData> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public void prepareView(ListView view)
    {

    }

    @Override
    public VisualizationAdapter<ListView, ListViewData> newInstance()
    {
        return new RunningApplicationTextAdapter();
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
