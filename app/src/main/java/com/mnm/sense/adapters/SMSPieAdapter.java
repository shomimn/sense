package com.mnm.sense.adapters;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.SMSContentListData;

public class SMSPieAdapter extends ContentPieAdapter
{
    public SMSPieAdapter(String key)
    {
        super(key);
    }

    @Override
    public PieData adaptOne(SensorData data)
    {
        SMSContentListData smsData = (SMSContentListData) data;

        return adaptImpl(smsData.getContentList());
    }

    @Override
    public void prepareView(PieChart view)
    {

    }
}
