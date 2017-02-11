package com.mnm.sense.adapters;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.mnm.sense.adapters.ContentPieAdapter;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.CallContentListData;

public class CallsPieAdapter extends ContentPieAdapter
{
    public CallsPieAdapter(String key)
    {
        super(key);
    }

    @Override
    public PieData adaptOne(SensorData data)
    {
        CallContentListData callsData = (CallContentListData) data;

        return adaptImpl(callsData.getContentList());
    }

    @Override
    public void prepareView(PieChart view)
    {

    }
}
