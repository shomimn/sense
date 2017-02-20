package com.mnm.sense.trackers;

import android.util.Log;

import com.mnm.sense.NotificationCreator;
import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.ActivityPieAdapter;
import com.mnm.sense.adapters.ActivityTextAdapter;
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

        limit = new Limit("Daily goal", 45, 1, 500);

        ActivityPieAdapter pieAdapter = new ActivityPieAdapter();
        pieAdapter.setLimit(limit.value);

        build()
            .text(new Visualization(1, 3, false))
            .pieChart(new Visualization(2, 3, false))
            .attribute(ATTRIBUTE_ACTIVITY)
            .adapters(new ActivityTextAdapter(),
                    pieAdapter);
    }

    @Override
    public void limitNotification(SensorData data)
    {
        checkGoal();
    }


    private void checkGoal()
    {
        ActivityPieAdapter pieAdapter = (ActivityPieAdapter)adapter(ATTRIBUTE_ACTIVITY, Visualization.PIE_CHART);

        int totalTime = pieAdapter.getTotalTime();

        if (totalTime >= limit.value && first)
        {
            first = false;
            NotificationCreator.create(resource, "Sense", "Congrats, you reached your daily goal!");
        }
    }

    @Override
    public void purge()
    {
        first = true;
        ActivityPieAdapter pieAdapter = (ActivityPieAdapter)adapter(ATTRIBUTE_ACTIVITY, Visualization.PIE_CHART);
        pieAdapter.resetTimes();

        super.purge();
    }
}
