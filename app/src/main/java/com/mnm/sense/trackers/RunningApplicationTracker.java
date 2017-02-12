package com.mnm.sense.trackers;

import com.mnm.sense.R;
import com.mnm.sense.Visualization;
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

        visualizations.put(Visualization.TEXT, new Visualization(3, 3, false));

        HashMap<String, VisualizationAdapter> nameAdapters = new HashMap<>();

        nameAdapters.put(Visualization.TEXT, new RunningApplicationTextAdapter());

        adapters.put(ATTRIBUTE_TOTAL, nameAdapters);
    }
}
