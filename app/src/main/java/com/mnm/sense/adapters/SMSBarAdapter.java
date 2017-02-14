package com.mnm.sense.adapters;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.mnm.sense.Util;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.ubhave.sensormanager.config.pull.ContentReaderConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderEntry;
import com.ubhave.sensormanager.data.pull.SMSContentListData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SMSBarAdapter extends ContentBarAdapter
{
    public SMSBarAdapter(String key)
    {
        super(key);
    }

    @Override
    public BarData adaptOne(SensorData data)
    {
        SMSContentListData smsData = (SMSContentListData) data;

        return adaptImpl(smsData.getContentList());
    }

    @Override
    public void prepareView(BarChart view)
    {

    }

    @Override
    public VisualizationAdapter<BarChart, BarData> newInstance()
    {
        return new SMSBarAdapter(key);
    }
}
