package com.mnm.sense.adapters;

import com.mnm.sense.map.AttributedFeature;
import com.mnm.sense.R;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderEntry;

public class CallsLatLngAdapter extends ContentLatLngAdapter
{
    @Override
    public void populate(AttributedFeature attr, AbstractContentReaderEntry entry)
    {
        attr.text("Calls")
            .origin(R.drawable.ic_phone_in_talk_black_48dp)
            .custom("Duration:", entry.get("duration") + " s");
    }

    @Override
    public CallsLatLngAdapter newInstance()
    {
        return new CallsLatLngAdapter();
    }
}
