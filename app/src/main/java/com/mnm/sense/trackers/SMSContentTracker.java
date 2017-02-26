package com.mnm.sense.trackers;

import com.mnm.sense.ContentLocator;
import com.mnm.sense.R;
import com.mnm.sense.Util;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.SMSBarAdapter;
import com.mnm.sense.adapters.SMSLatLngAdapter;
import com.mnm.sense.adapters.SMSPersonTextAdapter;
import com.mnm.sense.adapters.SMSPieAdapter;
import com.mnm.sense.adapters.SMSTypeTextAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.pull.ContentReaderConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.AbstractContentReaderListData;
import com.ubhave.sensormanager.sensors.SensorUtils;

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
                    new SMSLatLngAdapter()
            )
            .attribute(ATTRIBUTE_PERSON)
            .adapters(new SMSPersonTextAdapter(),
                    new SMSBarAdapter("person"),
                    new SMSPieAdapter("person"),
                    new SMSLatLngAdapter()
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

