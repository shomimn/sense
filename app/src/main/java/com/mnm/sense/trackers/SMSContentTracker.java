package com.mnm.sense.trackers;

import android.util.Pair;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.mnm.sense.ContentLocator;
import com.mnm.sense.R;
import com.mnm.sense.Util;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.SMSBarAdapter;
import com.mnm.sense.adapters.SMSPersonTextAdapter;
import com.mnm.sense.adapters.SMSPieAdapter;
import com.mnm.sense.adapters.SMSTypeTextAdapter;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.pull.ContentReaderConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderEntry;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderListData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class SMSContentTracker extends Tracker
{
    public static final String ATTRIBUTE_TYPE = "Type";
    public static final String ATTRIBUTE_PERSON = "Person";

    private ContentLocator locator = new ContentLocator();

    public SMSContentTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_SMS_CONTENT_READER);

        text = "SMS";
        resource = R.drawable.ic_sms_black_48dp;
        isOn = false;

        accent = R.color.greenColorAccent;
        theme = R.style.GreenTheme;

        attributes = new String[]{ ATTRIBUTE_TYPE, ATTRIBUTE_PERSON };

        build()
            .text(new Visualization(1, 1, false))
            .barChart(new Visualization(2, 3, false))
            .pieChart(new Visualization(2, 3, false))
            .map(new Visualization(2, 3, false))
            .attribute(ATTRIBUTE_TYPE)
            .adapters(new SMSTypeTextAdapter(),
                    new SMSBarAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY),
                    new SMSPieAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY),
                    new ContentLatLngAdapter()
            )
            .attribute(ATTRIBUTE_PERSON)
            .adapters(new SMSPersonTextAdapter(),
                    new SMSBarAdapter("person"),
                    new SMSPieAdapter("person"),
                    new ContentLatLngAdapter()
            );
    }

    @Override
    public void start() throws ESException
    {
        sensorManager().setSensorConfig(type, ContentReaderConfig.TIME_LIMIT_MILLIS, Util.today());

        super.start();
    }

    @Override
    protected void attachLocation(SensorData data)
    {
        locator.attachLocation((AbstractContentReaderListData) data);
    }
}

class ContentLatLngAdapter extends VisualizationAdapter<GoogleMap, ArrayList<LatLng>>
{
    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        return adaptOne(data.get(data.size() - 1));
    }

    @Override
    public ArrayList<LatLng> adaptOne(SensorData data)
    {
        AbstractContentReaderListData listData = (AbstractContentReaderListData) data;
        ArrayList<LatLng> result = new ArrayList<>();

        for (AbstractContentReaderEntry entry : listData.getContentList())
        {
            Pair<Double, Double> location = entry.getLocation();

            if (location != null)
                result.add(new LatLng(location.first, location.second));
        }

        return result;
    }

    @Override
    public ArrayList<ArrayList<LatLng>> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public void prepareView(GoogleMap view)
    {

    }

    @Override
    public VisualizationAdapter<GoogleMap, ArrayList<LatLng>> newInstance()
    {
        return new ContentLatLngAdapter();
    }

    @Override
    public boolean isAggregating()
    {
        return true;
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        ArrayList<LatLng> result = new ArrayList<>();
        HashMap<String, ArrayList<SensorData>> dataByDays = partitionByDays(data);

        for (ArrayList<SensorData> dailyData : dataByDays.values())
            for (SensorData sensorData : dailyData)
                result.addAll(adaptOne(sensorData));

        return result;
    }
}

