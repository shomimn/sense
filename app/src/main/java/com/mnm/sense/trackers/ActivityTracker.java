package com.mnm.sense.trackers;

import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.ActivityTextAdapter;
import com.mnm.sense.adapters.RunningApplicationBarAdapter;
import com.mnm.sense.adapters.RunningApplicationTextAdapter;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.HashMap;

public class ActivityTracker extends Tracker
{
    private static final String ATTRIBUTE_TEST = "Test";

    public ActivityTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_ACTIVITY_RECOGNITION);

        text = "Activity";
        resource = R.drawable.ic_directions_run_black_48dp;
        isOn = false;

        attributes = new String[]{ATTRIBUTE_TEST};

        visualizations.put(Visualization.TEXT, new Visualization(1, 3, false));

        HashMap<String, VisualizationAdapter> totalTimeAdapters = new HashMap<>();

        totalTimeAdapters.put(Visualization.TEXT, new ActivityTextAdapter());

        adapters.put(ATTRIBUTE_TEST, totalTimeAdapters);
    }
}
