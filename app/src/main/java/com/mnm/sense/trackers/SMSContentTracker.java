package com.mnm.sense.trackers;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
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

