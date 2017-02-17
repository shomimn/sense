package com.mnm.sense.trackers;


import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.BatteryLineAdapter;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.HashMap;

class VisualizationBuilder
{
    private Tracker _tracker;
    private String _attribute;
    private String _visualizationType;

    public VisualizationBuilder(Tracker t)
    {
        _tracker = t;
    }

    public VisualizationBuilder attribute(String attr)
    {
        _attribute = attr;

        return this;
    }

    public VisualizationBuilder visualization(String type, Visualization visualization)
    {
        _visualizationType = type;

        _tracker.adapters.put(_attribute, new HashMap<String, VisualizationAdapter>());
        _tracker.visualizations.put(type, visualization);

        return this;
    }

    public VisualizationBuilder text(Visualization visualization)
    {
        return visualization(Visualization.TEXT, visualization);
    }

    public VisualizationBuilder barChart(Visualization visualization)
    {
        return visualization(Visualization.BAR_CHART, visualization);
    }

    public VisualizationBuilder pieChart(Visualization visualization)
    {
        return visualization(Visualization.PIE_CHART, visualization);
    }

    public VisualizationBuilder lineChart(Visualization visualization)
    {
        return visualization(Visualization.LINE_CHART, visualization);
    }

    public VisualizationBuilder listView(Visualization visualization)
    {
        return visualization(Visualization.LIST_VIEW, visualization);
    }

    public VisualizationBuilder map(Visualization visualization)
    {
        return visualization(Visualization.MAP, visualization);
    }

    public VisualizationBuilder adapters(VisualizationAdapter... adapters)
    {
        for (VisualizationAdapter adapter : adapters)
        {
            _tracker.adapters.get(_attribute).put(_visualizationType, adapter);
        }

        return this;
    }

    public void build()
    {

    }
}

public class BatteryTracker extends Tracker
{
    public static final String ATTRIBUTE_PERCENT = "Percent";

    public BatteryTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_BATTERY);

        text = "Battery";
        resource = R.drawable.ic_battery_full_black_48dp;
        isOn = false;

        accent = R.color.redColorAccent;
        theme = R.style.RedTheme;

        attributes = new String[]{ ATTRIBUTE_PERCENT };

        visualizations.put(Visualization.LINE_CHART, new Visualization(2, 3, false));

        HashMap<String, VisualizationAdapter> percentAdapters = new HashMap<>();
        percentAdapters.put(Visualization.LINE_CHART, new BatteryLineAdapter());

        adapters.put(ATTRIBUTE_PERCENT, percentAdapters);
    }
}

