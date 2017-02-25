package com.mnm.sense.trackers;

import android.location.Location;
import android.util.Pair;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.mnm.sense.R;
import com.mnm.sense.Util;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.BatteryLineAdapter;
import com.mnm.sense.adapters.ContentAdapter;
import com.mnm.sense.adapters.SMSBarAdapter;
import com.mnm.sense.adapters.SMSPersonTextAdapter;
import com.mnm.sense.adapters.SMSPieAdapter;
import com.mnm.sense.adapters.SMSTypeTextAdapter;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.mnm.sense.models.BarChartModel;
import com.mnm.sense.models.PieChartModel;
import com.mnm.sense.models.TextModel;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.pull.ContentReaderConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.LocationData;
import com.ubhave.sensormanager.data.pull.SMSContentListData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SMSContentTracker extends Tracker
{
    public static final String ATTRIBUTE_TYPE = "Type";
    public static final String ATTRIBUTE_PERSON = "Person";

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
            .attribute(ATTRIBUTE_TYPE)
            .adapters(new SMSTypeTextAdapter(),
                    new SMSBarAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY),
                    new SMSPieAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY))
            .attribute(ATTRIBUTE_PERSON)
            .adapters(new SMSPersonTextAdapter(),
                    new SMSBarAdapter("person"),
                    new SMSPieAdapter("person"));

    }

    @Override
    public void start() throws ESException
    {
        sensorManager().setSensorConfig(type, ContentReaderConfig.TIME_LIMIT_MILLIS, Util.today());

        super.start();
    }
}

class SMSLatLngAdapter extends VisualizationAdapter<GoogleMap, LatLng>
{
    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        LatLng latLng = adaptOne(data.get(data.size() - 1));
        ArrayList<LatLng> result = new ArrayList<>();

        result.add(latLng);

        return result;
    }

    @Override
    public LatLng adaptOne(SensorData data)
    {
        SMSContentListData smsData = (SMSContentListData) data;
        Pair<Double, Double> location = smsData.getLocation();

        return new LatLng(location.first, location.second);
    }

    @Override
    public ArrayList<LatLng> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public void prepareView(GoogleMap view)
    {

    }

    @Override
    public VisualizationAdapter<GoogleMap, LatLng> newInstance()
    {
        return new com.mnm.sense.adapters.LocationLatLngAdapter();
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        return null;
    }
}

