package com.mnm.sense.trackers;

import android.util.Log;

import com.mnm.sense.NotificationCreator;
import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.ActivityPieAdapter;
import com.mnm.sense.adapters.ActivityTextAdapter;
import com.mnm.sense.adapters.RunningApplicationBarAdapter;
import com.mnm.sense.adapters.RunningApplicationTextAdapter;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.HashMap;

public class ActivityTracker extends Tracker
{
    private static final String ATTRIBUTE_ACTIVITY = "Activity";
    private boolean first = true;

    public ActivityTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_ACTIVITY_RECOGNITION);

        text = "Activity";
        resource = R.drawable.ic_directions_run_black_48dp;
        isOn = false;

        attributes = new String[]{ATTRIBUTE_ACTIVITY};

        visualizations.put(Visualization.TEXT, new Visualization(1, 3, false));
        visualizations.put(Visualization.PIE_CHART, new Visualization(2, 3, false));

        limit = new Limit("Daily goal", 45, 1, 500);

        HashMap<String, VisualizationAdapter> activityAdapters = new HashMap<>();

        activityAdapters.put(Visualization.TEXT, new ActivityTextAdapter());
        activityAdapters.put(Visualization.PIE_CHART, new ActivityPieAdapter());

        ((ActivityPieAdapter)activityAdapters.get(Visualization.PIE_CHART)).setActivityGoal(limit.value);

        adapters.put(ATTRIBUTE_ACTIVITY, activityAdapters);


    }

    @Override
    public void limitNotification(SensorData data)
    {
        checkGoal();
    }


    public void checkGoal()
    {
        ActivityPieAdapter pieAdapter = (ActivityPieAdapter)adapters.get(ATTRIBUTE_ACTIVITY).get(Visualization.PIE_CHART);
        pieAdapter.setActivityGoal(limit.value);

        int totalTime = pieAdapter.getTotalTime();

        if (totalTime >= limit.value && first)
        {
            first = false;
            NotificationCreator.create(resource, "Sense", "Congrats, you reached your daily goal!");
        }
    }
}
