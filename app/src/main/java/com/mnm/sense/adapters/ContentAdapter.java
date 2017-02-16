package com.mnm.sense.adapters;

import android.util.LongSparseArray;

import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderEntry;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderListData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public abstract class ContentAdapter<T, U> extends VisualizationAdapter<T, U>
{
    protected String key;

    public ContentAdapter(String k)
    {
        key = k;
    }

    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        int last = data.size() - 1;

        return adaptOne(data.get(last));
    }

    protected U adaptImpl(ArrayList<AbstractContentReaderEntry> data)
    {
        HashMap<String, Integer> counter = new HashMap<>();

        for (AbstractContentReaderEntry entry : data)
        {
            String type = entry.get(key);

            if (counter.containsKey(type))
                counter.put(type, counter.get(type) + 1);
            else
                counter.put(type, 1);
        }

        return createFrom(counter);
    }

    protected abstract U createFrom(HashMap<String, Integer> counter);

    @Override
    public ArrayList<U> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public boolean isAggregating()
    {
        return true;
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        HashMap<String, ArrayList<SensorData>> dataByDays = partitionByDays(data);
        HashMap<String, Integer> counter = new HashMap<>();

        for (ArrayList<SensorData> dailyData : dataByDays.values())
        {
            AbstractContentReaderListData last = (AbstractContentReaderListData) dailyData.get(dailyData.size() - 1);

            for (AbstractContentReaderEntry entry : last.getContentList())
            {
                String type = entry.get(key);

                if (counter.containsKey(type))
                    counter.put(type, counter.get(type) + 1);
                else
                    counter.put(type, 1);
            }
        }

        return createFrom(counter);
    }
}
