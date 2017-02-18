package com.mnm.sense.adapters;

import com.mnm.sense.trackers.Tracker;
import com.ubhave.sensormanager.data.SensorData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public abstract class VisualizationAdapter<T, U>
{
    public abstract Object adapt(ArrayList<SensorData> data);

    public abstract U adaptOne(SensorData data);
    public abstract ArrayList<U> adaptAll(ArrayList<SensorData> data);

    public void prepareView(T view)
    {
    }

    public abstract VisualizationAdapter<T, U> newInstance();

    public boolean isAggregating()
    {
        return false;
    }

    public abstract Object aggregate(ArrayList<SensorData> data);

    protected HashMap<String, ArrayList<SensorData>> partitionByDays(ArrayList<SensorData> data)
    {
        Collections.sort(data, new Comparator<SensorData>()
        {
            @Override
            public int compare(SensorData a, SensorData b)
            {
                long ts1 = a.getTimestamp();
                long ts2 = b.getTimestamp();

                return ts1 < ts2 ? -1 : ts1 == ts2 ? 0 : 1;
            }
        });

        HashMap<String, ArrayList<SensorData>> dataByDays = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();

        for (SensorData sensorData : data)
        {
            calendar.setTimeInMillis(sensorData.getTimestamp());
            String key = dateFormat.format(calendar.getTime());

            if (dataByDays.get(key) == null)
                dataByDays.put(key, new ArrayList<SensorData>());

            dataByDays.get(key).add(sensorData);
        }

        return dataByDays;
    }

    public boolean useLimit()
    {
        return false;
    }
    
    public void setLimit(int limit)
    {
        
    }
}
