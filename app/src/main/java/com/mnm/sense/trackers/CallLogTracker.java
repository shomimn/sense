package com.mnm.sense.trackers;

import com.mnm.sense.R;
import com.mnm.sense.Util;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.CallsBarAdapter;
import com.mnm.sense.adapters.CallsPersonTextAdapter;
import com.mnm.sense.adapters.CallsPieAdapter;
import com.mnm.sense.adapters.CallsTypeTextAdapter;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.pull.ContentReaderConfig;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.Calendar;
import java.util.HashMap;

public class CallLogTracker extends Tracker
{
    public static final String ATTRIBUTE_TYPE = "Type";
    public static final String ATTRIBUTE_PERSON = "Person";

    public CallLogTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_CALL_CONTENT_READER);

        text = "Calls";
        resource = R.drawable.ic_phone_in_talk_black_48dp;
        isOn = false;

        accent = R.color.greenColorAccent;
        theme = R.style.GreenTheme;

        attributes = new String[]{ ATTRIBUTE_TYPE, ATTRIBUTE_PERSON };

        build()
            .text(new Visualization(1, 1, false))
            .pieChart(new Visualization(2, 3, false))
            .barChart(new Visualization(1, 3, false))
            .attribute(ATTRIBUTE_TYPE)
            .adapters(new CallsTypeTextAdapter(),
                    new CallsPieAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY),
                    new CallsBarAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY))
            .attribute(ATTRIBUTE_PERSON)
            .adapters(new CallsPersonTextAdapter(),
                    new CallsPieAdapter("person"),
                    new CallsBarAdapter("person"));
    }

    @Override
    public void start() throws ESException
    {
        sensorManager().setSensorConfig(type, ContentReaderConfig.TIME_LIMIT_MILLIS, Util.today());

        super.start();
    }
}

