package com.mnm.sense.adapters;

import com.mnm.sense.Util;
import com.mnm.sense.map.AttributedFeature;
import com.mnm.sense.R;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderEntry;
import com.ubhave.sensormanager.process.pull.CallContentReaderProcessor;

import java.util.HashMap;

public class CallsLatLngAdapter extends ContentLatLngAdapter
{
    private HashMap<String, Integer> resources = new HashMap<>();

    public CallsLatLngAdapter()
    {
        resources.put(CallContentReaderProcessor.INCOMING, R.drawable.call_incoming);
        resources.put(CallContentReaderProcessor.OUTGOING, R.drawable.call_outgoing);
        resources.put(CallContentReaderProcessor.MISSED, R.drawable.call_missed);
        resources.put(CallContentReaderProcessor.REJECTED, R.drawable.call_rejected);
    }

    @Override
    public void populate(AttributedFeature attr, AbstractContentReaderEntry entry, String type)
    {
        Integer resource = resources.get(type);

        if (resource == null)
            resource = R.drawable.ic_phone_in_talk_black_48dp;

        attr.text("Calls")
            .origin(R.drawable.ic_phone_in_talk_black_48dp)
            .icon(Util.bitmapFromResource(resource))
            .custom("Duration:", entry.get("duration") + " s");
    }

    @Override
    public CallsLatLngAdapter newInstance()
    {
        return new CallsLatLngAdapter();
    }
}
