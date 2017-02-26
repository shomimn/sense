package com.mnm.sense.trackers;

import com.mnm.sense.ContentLocator;
import com.mnm.sense.R;
import com.mnm.sense.Util;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.CallsBarAdapter;
import com.mnm.sense.adapters.CallsLatLngAdapter;
import com.mnm.sense.adapters.CallsPersonTextAdapter;
import com.mnm.sense.adapters.CallsPieAdapter;
import com.mnm.sense.adapters.CallsTypeTextAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.pull.ContentReaderConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderListData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class CallLogTracker extends Tracker
{
    public static final String ATTRIBUTE_TYPE = "Type";
    public static final String ATTRIBUTE_PERSON = "Person";

    private ContentLocator locator = new ContentLocator();

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
            .map(new Visualization(2, 3, false))
            .attribute(ATTRIBUTE_TYPE)
            .adapters(new CallsTypeTextAdapter(),
                    new CallsPieAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY),
                    new CallsBarAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY),
                    new CallsLatLngAdapter()
            )
            .attribute(ATTRIBUTE_PERSON)
            .adapters(new CallsPersonTextAdapter(),
                    new CallsPieAdapter("person"),
                    new CallsBarAdapter("person"),
                    new CallsLatLngAdapter()
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

