package com.mnm.sense.trackers;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;
import com.mnm.sense.R;
import com.mnm.sense.Visualization;
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
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.Calendar;
import java.util.HashMap;

public class SMSContentTracker extends Tracker
{
    public SMSContentTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_SMS_CONTENT_READER);

        text = "SMS";
        resource = R.drawable.ic_sms_black_48dp;
        isOn = false;

        attributes = new String[]{ "Type", "Person" };

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

//        adapters.put(Visualization.TEXT, new SMSTypeTextAdapter());
//        adapters.put(Visualization.BAR_CHART, new SMSBarAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY));
//        adapters.put(Visualization.PIE_CHART, new SMSPieAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY));

        adapters.put("Type", typeAdapters);
        adapters.put("Person", personAdapters);
    }

    @Override
    public void start() throws ESException
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        sensorManager().setSensorConfig(type, ContentReaderConfig.TIME_LIMIT_MILLIS, cal.getTimeInMillis());

        super.start();
    }

    @Override
    public Object getModel(String visualizationType)
    {
        if (visualizationType.equals(Visualization.TEXT))
            return new TextModel(this, (String) super.getModel(visualizationType));
        else if (visualizationType.equals(Visualization.BAR_CHART))
            return new BarChartModel(this, (BarData) super.getModel(visualizationType));
        else if (visualizationType.equals(Visualization.PIE_CHART))
            return new PieChartModel(this, (PieData) super.getModel(visualizationType));

        return null;
    }

    @Override
    public Object getModel(String attribute, String visualizationType)
    {
        if (visualizationType.equals(Visualization.TEXT))
            return new TextModel(this, (String) super.getModel(attribute, visualizationType));
        else if (visualizationType.equals(Visualization.PIE_CHART))
            return new PieChartModel(this, (PieData) super.getModel(attribute, visualizationType));
        else if (visualizationType.equals(Visualization.BAR_CHART))
            return new BarChartModel(this, (BarData) super.getModel(attribute, visualizationType));

        return null;
    }
}

