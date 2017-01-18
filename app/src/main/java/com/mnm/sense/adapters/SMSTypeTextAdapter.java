package com.mnm.sense.adapters;

import com.ubhave.sensormanager.config.pull.ContentReaderConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.SMSContentListData;

public class SMSTypeTextAdapter extends SMSTextAdapter
{
    public SMSTypeTextAdapter()
    {
        super(ContentReaderConfig.SMS_CONTENT_TYPE_KEY);
    }
}

