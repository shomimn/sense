package com.mnm.sense.adapters;

import com.mnm.sense.map.AttributedPosition;
import com.mnm.sense.R;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderEntry;

public class SMSLatLngAdapter extends ContentLatLngAdapter
{
    @Override
    public void populate(AttributedPosition attr, AbstractContentReaderEntry entry)
    {
        attr.text("SMS")
            .origin(R.drawable.ic_sms_black_48dp)
            .custom("Length:", entry.get("bodyLength"))
            .custom("Word count:", entry.get("bodyWordCount"));
    }

    @Override
    public SMSLatLngAdapter newInstance()
    {
        return new SMSLatLngAdapter();
    }
}
