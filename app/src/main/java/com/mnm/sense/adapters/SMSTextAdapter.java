package com.mnm.sense.adapters;

import android.widget.TextView;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.SMSContentListData;

public class SMSTextAdapter extends ContentTextAdapter
{
    public SMSTextAdapter(String key)
    {
        super(key);
    }

    @Override
    public String adaptOne(SensorData data)
    {
        SMSContentListData smsData = (SMSContentListData) data;

        return adaptImpl(smsData.getContentList());
    }

    @Override
    public void prepareView(TextView view)
    {

    }

    @Override
    public VisualizationAdapter<TextView, String> newInstance()
    {
        return new SMSTextAdapter(key);
    }
}
