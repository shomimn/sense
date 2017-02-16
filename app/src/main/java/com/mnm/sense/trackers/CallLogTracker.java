package com.mnm.sense.trackers;

import com.mnm.sense.R;
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

        visualizations.put(Visualization.TEXT, new Visualization(1, 1, false));
        visualizations.put(Visualization.PIE_CHART, new Visualization(2, 3, false));
        visualizations.put(Visualization.BAR_CHART, new Visualization(1, 3, false));

        HashMap<String, VisualizationAdapter> typeAdapters = new HashMap<>();
        typeAdapters.put(Visualization.TEXT, new CallsTypeTextAdapter());
        typeAdapters.put(Visualization.PIE_CHART, new CallsPieAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY));
        typeAdapters.put(Visualization.BAR_CHART, new CallsBarAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY));

        HashMap<String, VisualizationAdapter> personAdapters = new HashMap<>();
        personAdapters.put(Visualization.TEXT, new CallsPersonTextAdapter());
        personAdapters.put(Visualization.PIE_CHART, new CallsPieAdapter("person"));
        personAdapters.put(Visualization.BAR_CHART, new CallsBarAdapter("person"));

        adapters.put(ATTRIBUTE_TYPE, typeAdapters);
        adapters.put(ATTRIBUTE_PERSON, personAdapters);
    }

    @Override
    public void start() throws ESException
    {
        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        sensorManager().setSensorConfig(type, ContentReaderConfig.TIME_LIMIT_MILLIS, cal.getTimeInMillis());

        super.start();
    }
}

