package com.mnm.sense.trackers;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;
import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.CallsPersonTextAdapter;
import com.mnm.sense.adapters.CallsTypeTextAdapter;
import com.mnm.sense.adapters.ContentBarAdapter;
import com.mnm.sense.adapters.ContentPieAdapter;
import com.mnm.sense.models.BarChartModel;
import com.mnm.sense.models.PieChartModel;
import com.mnm.sense.models.TextModel;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.pull.ContentReaderConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.CallContentListData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.Calendar;

public class CallLogTracker extends Tracker
{
    public CallLogTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_CALL_CONTENT_READER);

        text = "Calls";
        resource = R.drawable.ic_phone_in_talk_black_48dp;
        isOn = false;

        visualizations.put(Visualization.TEXT, new Visualization(1, 1, false));
        visualizations.put(Visualization.PIE_CHART, new Visualization(2, 3, false));
        visualizations.put(Visualization.BAR_CHART, new Visualization(1, 3, false));

        adapters.put(Visualization.TEXT, new CallsTypeTextAdapter());
        adapters.put(Visualization.PIE_CHART, new CallsPieAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY));
        adapters.put(Visualization.BAR_CHART, new CallsBarAdapter(ContentReaderConfig.SMS_CONTENT_TYPE_KEY));
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
        else if (visualizationType.equals(Visualization.PIE_CHART))
            return new PieChartModel(this, (PieData) super.getModel(visualizationType));
        else if (visualizationType.equals(Visualization.BAR_CHART))
            return new BarChartModel(this, (BarData) super.getModel(visualizationType));

        return null;
    }
}

class CallsPieAdapter extends ContentPieAdapter
{
    public CallsPieAdapter(String key)
    {
        super(key);
    }

    @Override
    public PieData adaptOne(SensorData data)
    {
        CallContentListData callsData = (CallContentListData) data;

        return adaptImpl(callsData.getContentList());
    }

    @Override
    public void prepareView(PieChart view)
    {

    }
}

class CallsBarAdapter extends ContentBarAdapter
{
    public CallsBarAdapter(String key)
    {
        super(key);
    }

    @Override
    public BarData adaptOne(SensorData data)
    {
        CallContentListData callsData = (CallContentListData) data;

        return adaptImpl(callsData.getContentList());
    }

    @Override
    public void prepareView(BarChart view)
    {

    }
}

