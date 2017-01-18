package com.mnm.sense.adapters;

import android.widget.TextView;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class ContentTextAdapter extends ContentAdapter<TextView, String>
{
    public ContentTextAdapter(String key)
    {
        super(key);
    }

    @Override
    protected String createFrom(HashMap<String, Integer> counter)
    {
        String result = "";

        for (Map.Entry<String, Integer> entry : counter.entrySet())
            result += entry.getKey() + ": " +
                    String.valueOf(entry.getValue()) + "\n";

        return result;
    }
}
