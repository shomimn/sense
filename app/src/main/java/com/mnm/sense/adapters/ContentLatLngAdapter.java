package com.mnm.sense.adapters;

import android.util.Pair;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.mnm.sense.map.AttributedFeature;
import com.mnm.sense.Timestamp;
import com.ubhave.sensormanager.config.pull.ContentReaderConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderEntry;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderListData;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class ContentLatLngAdapter extends VisualizationAdapter<GoogleMap, ArrayList<AttributedFeature>>
{
    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        return adaptOne(data.get(data.size() - 1));
    }

    @Override
    public ArrayList<AttributedFeature> adaptOne(SensorData data)
    {
        AbstractContentReaderListData listData = (AbstractContentReaderListData) data;
        ArrayList<AttributedFeature> result = new ArrayList<>();

        for (AbstractContentReaderEntry entry : listData.getContentList())
        {
            Pair<Double, Double> location = entry.getLocation();

            if (location != null)
            {
                LatLng latLng = new LatLng(location.first, location.second);
                String text = entry.toString();
                long date = Long.parseLong(entry.get(ContentReaderConfig.SMS_CONTENT_DATE_KEY));

                AttributedFeature attr = new AttributedFeature()
                        .latLng(latLng)
                        .custom("Type:", entry.get(ContentReaderConfig.SMS_CONTENT_TYPE_KEY))
                        .custom("Contact:", entry.get("person"))
                        .custom("Date:", Timestamp.from(date).date())
                        .custom("Time:", Timestamp.from(date).time());

                populate(attr, entry);

                result.add(attr);
            }
        }

        return result;
    }

    @Override
    public ArrayList<ArrayList<AttributedFeature>> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public void prepareView(GoogleMap view)
    {

    }

    @Override
    public boolean isAggregating()
    {
        return true;
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        ArrayList<AttributedFeature> result = new ArrayList<>();
        HashMap<String, ArrayList<SensorData>> dataByDays = partitionByDays(data);

        for (ArrayList<SensorData> dailyData : dataByDays.values())
        {
            SensorData last = dailyData.get(dailyData.size() - 1);

            result.addAll(adaptOne(last));
        }

        return result;
    }

    public abstract void populate(AttributedFeature attr, AbstractContentReaderEntry entry);
}
