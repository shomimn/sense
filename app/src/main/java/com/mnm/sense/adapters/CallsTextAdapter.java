package com.mnm.sense.adapters;

import android.widget.TextView;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.CallContentListData;

public class CallsTextAdapter extends ContentTextAdapter
{
    public CallsTextAdapter(String key)
    {
        super(key);
    }

    @Override
    public String adaptOne(SensorData data)
    {
        CallContentListData smsData = (CallContentListData) data;

        return adaptImpl(smsData.getContentList());
    }

    @Override
    public void prepareView(TextView view)
    {

    }
}
