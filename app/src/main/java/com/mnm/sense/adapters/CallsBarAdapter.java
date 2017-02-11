package com.mnm.sense.adapters;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.mnm.sense.adapters.ContentBarAdapter;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.CallContentListData;

public class CallsBarAdapter extends ContentBarAdapter
{
    public CallsBarAdapter(String key)
    {
        super(key);
    }

    @Override
    public BarData adaptOne(SensorData data)
    {
        CallContentListData callsData = (CallContentListData) data;

        return adaptImpl(callsData.getContentList());
    }

    @Override
    public void prepareView(BarChart view)
    {

    }
}
