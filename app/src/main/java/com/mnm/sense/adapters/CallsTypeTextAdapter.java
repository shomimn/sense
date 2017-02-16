package com.mnm.sense.adapters;

import android.widget.TextView;

import com.ubhave.sensormanager.config.pull.ContentReaderConfig;

public class CallsTypeTextAdapter extends CallsTextAdapter
{
    public CallsTypeTextAdapter()
    {
        super(ContentReaderConfig.SMS_CONTENT_TYPE_KEY);
    }

    @Override
    public VisualizationAdapter<TextView, String> newInstance()
    {
        return new CallsTypeTextAdapter();
    }
}

