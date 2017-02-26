package com.mnm.sense.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.mnm.sense.R;
import com.mnm.sense.SenseApp;
import com.mnm.sense.Timestamp;
import com.mnm.sense.Util;
import com.mnm.sense.map.AttributedFeature;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.MicrophoneData;

import java.util.ArrayList;

public class MicrophoneLatLngAdapter extends VisualizationAdapter<GoogleMap, ArrayList<AttributedFeature>>
{

    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if(data.size() == 0)
            return null;

        ArrayList<ArrayList<AttributedFeature>> result = adaptAll(data);

        if(result.size() == 0)
            return 0;
        return result.get(0);

    }

    @Override
    public ArrayList<AttributedFeature> adaptOne(SensorData data)
    {
        return null;
    }

    @Override
    public ArrayList<ArrayList<AttributedFeature>> adaptAll(ArrayList<SensorData> dataList)
    {
        ArrayList<AttributedFeature> result = new ArrayList<>();

        for(SensorData data : dataList)
        {
            MicrophoneData micData = (MicrophoneData) data;

            Pair<Double, Double> location = micData.getLocation();

            if(location != null)
            {
                LatLng latLng = new LatLng(location.first, location.second);

                Bitmap icon = Util.bitmapFromResource(R.drawable.ic_hearing_black_48dp, R.color.redColorAccent);

                result.add(new AttributedFeature()
                        .origin(R.drawable.ic_mic_black_48dp)
                        .icon(icon)
                        .text("Hearing damage")
                        .latLng(latLng)
                        .custom("Date:", Timestamp.from(micData.getTimestamp()).date())
                        .custom("Time:", Timestamp.from(micData.getTimestamp()).time())
                        .custom("Decibels:", String.valueOf(micData.getAverageDecibels() + " dBFS")));
            }
        }

        ArrayList<ArrayList<AttributedFeature>> finalResult = new ArrayList<>();
        finalResult.add(result);

        return finalResult;
    }

    @Override
    public VisualizationAdapter<GoogleMap, ArrayList<AttributedFeature>> newInstance()
    {
        return null;
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        return null;
    }
}
