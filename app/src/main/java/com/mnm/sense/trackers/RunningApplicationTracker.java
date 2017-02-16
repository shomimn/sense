package com.mnm.sense.trackers;

import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.RunningApplicationBarAdapter;
import com.mnm.sense.adapters.RunningApplicationPieAdapter;
import com.mnm.sense.adapters.RunningApplicationTextAdapter;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.mnm.sense.models.TextModel;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.HashMap;

public class RunningApplicationTracker extends Tracker
{
    private static final String ATTRIBUTE_TOTAL = "Total time";

    public RunningApplicationTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_RUNNING_APP);

        text = "Running Apps";
        resource = R.drawable.ic_dashboard_black_48dp;
        isOn = false;

        attributes = new String[]{ATTRIBUTE_TOTAL};

        visualizations.put(Visualization.LIST_VIEW, new Visualization(2, 3, false));
        visualizations.put(Visualization.BAR_CHART, new Visualization(2, 3, false));
        visualizations.put(Visualization.PIE_CHART, new Visualization(2, 3, false));

        HashMap<String, VisualizationAdapter> totalTimeAdapters = new HashMap<>();

        totalTimeAdapters.put(Visualization.LIST_VIEW, new RunningApplicationTextAdapter());
        totalTimeAdapters.put(Visualization.BAR_CHART, new RunningApplicationBarAdapter());
        totalTimeAdapters.put(Visualization.PIE_CHART, new RunningApplicationPieAdapter());



        adapters.put(ATTRIBUTE_TOTAL, totalTimeAdapters);
    }
}
