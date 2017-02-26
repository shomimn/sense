package com.mnm.sense;

import com.mnm.sense.adapters.VisualizationAdapter;
import com.mnm.sense.trackers.Tracker;

import java.util.ArrayList;
import java.util.HashMap;

public class VisualizationBuilder
{
    private Tracker _tracker;
    private String _attribute;
    private ArrayList<String> _visualizationTypes = new ArrayList<>();

    public VisualizationBuilder(Tracker t)
    {
        _tracker = t;
    }

    public VisualizationBuilder attribute(String attr)
    {
        _attribute = attr;
        _tracker.adapters.put(_attribute, new HashMap<String, VisualizationAdapter>());

        return this;
    }

    public VisualizationBuilder visualization(String type, Visualization visualization)
    {
        _visualizationTypes.add(type);
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

    public VisualizationBuilder clusteredMap(Visualization visualization)
    {
        return visualization(Visualization.CLUSTERED_MAP, visualization);
    }

    public VisualizationBuilder adapters(VisualizationAdapter... adapters)
    {
        for (int i = 0; i < adapters.length; ++i)
        {
            VisualizationAdapter adapter = adapters[i];
            String type = _visualizationTypes.get(i);

            _tracker.adapters.get(_attribute).put(type, adapter);
        }

        return this;
    }
}
