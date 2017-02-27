package com.mnm.sense.adapters;

import com.mnm.sense.Util;
import com.mnm.sense.map.AttributedFeature;
import com.mnm.sense.R;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderEntry;
import com.ubhave.sensormanager.process.pull.SMSContentReaderProcessor;

import java.util.HashMap;

public class SMSLatLngAdapter extends ContentLatLngAdapter
{
    private HashMap<String, Integer> resources = new HashMap<>();

    public SMSLatLngAdapter()
    {
        resources.put(SMSContentReaderProcessor.MESSAGE_TYPE_SENT, R.drawable.sms_outgoing);
        resources.put(SMSContentReaderProcessor.MESSAGE_TYPE_INBOX, R.drawable.sms_incoming);
        resources.put(SMSContentReaderProcessor.MESSAGE_TYPE_FAILED, R.drawable.ic_sms_failed_black_48dp);
    }

    @Override
    public void populate(AttributedFeature attr, AbstractContentReaderEntry entry, String type)
    {
        Integer resource = resources.get(type);

        if (resource == null)
            resource = R.drawable.ic_sms_black_48dp;

        attr.text("SMS")
            .origin(R.drawable.ic_sms_black_48dp)
            .icon(Util.bitmapFromResource(resource))
            .custom("Length:", entry.get("bodyLength"))
            .custom("Word count:", entry.get("bodyWordCount"));
    }

    @Override
    public SMSLatLngAdapter newInstance()
    {
        return new SMSLatLngAdapter();
    }
}
