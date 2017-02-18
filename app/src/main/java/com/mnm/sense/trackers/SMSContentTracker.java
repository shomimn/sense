package com.mnm.sense.trackers;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.mnm.sense.R;
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

        visualizations.put(Visualization.TEXT, new Visualization(1, 1, false));
        visualizations.put(Visualization.BAR_CHART, new Visualization(1, 3, false));
        visualizations.put(Visualization.PIE_CHART, new Visualization(2, 3, false));

        HashMap<String, VisualizationAdapter> typeAdapters = new HashMap<>();
        typeAdapters.put(Visualization.TEXT, new SMSTypeTextAdapter());
        typeAdapters.put(Visualization.BAR_CHART, new SMSBarAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY));
        typeAdapters.put(Visualization.PIE_CHART, new SMSPieAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY));

        HashMap<String, VisualizationAdapter> personAdapters = new HashMap<>();
        personAdapters.put(Visualization.TEXT, new SMSPersonTextAdapter());
        personAdapters.put(Visualization.BAR_CHART, new SMSBarAdapter("person"));
        personAdapters.put(Visualization.PIE_CHART, new SMSPieAdapter("person"));

        adapters.put(ATTRIBUTE_TYPE, typeAdapters);
        adapters.put(ATTRIBUTE_PERSON, personAdapters);

//        build()
//            .text(new Visualization(1, 1, false))
//            .barChart(new Visualization(2, 3, false))
//            .pieChart(new Visualization(2, 3, false))
//            .attribute(ATTRIBUTE_TYPE)
//            .adapters(new SMSTypeTextAdapter(),
//                    new SMSBarAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY),
//                    new SMSPieAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY))
//            .attribute(ATTRIBUTE_PERSON)
//            .adapters(new SMSPersonTextAdapter(),
//                    new SMSBarAdapter("person"),
//                    new SMSPieAdapter("person"));

    }

    private VisualizationBuilder build()
    {
        return new VisualizationBuilder(this);
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

