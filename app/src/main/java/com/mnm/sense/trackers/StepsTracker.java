package com.mnm.sense.trackers;

import android.content.SharedPreferences;

import com.mnm.sense.NotificationCreator;
import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.StepsDailyBarAdapter;
import com.mnm.sense.adapters.StepsHourlyBarAdapter;
import com.mnm.sense.adapters.StepsTextAdapter;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.StepCounterData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class StepsTracker extends Tracker
{
    public static final String ATTRIBUTE_HOURLY = "Hourly";
    public static final String ATTRIBUTE_DAILY = "Daily";

    float firstCount = 0;
    float steps = 0;

    public StepsTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_STEP_COUNTER);

        text = "Steps";
        resource = R.drawable.ic_directions_walk;
        isOn = false;

        limit = new Limit("Daily goal", 1000, 100, 20000);

        attributes = new String[] { ATTRIBUTE_HOURLY, ATTRIBUTE_DAILY };

        visualizations.put(Visualization.TEXT, new Visualization(1, 1, false));
        visualizations.put(Visualization.BAR_CHART, new Visualization(1, 3, false));

        HashMap<String, VisualizationAdapter> hourlyAdapters = new HashMap<>();
        hourlyAdapters.put(Visualization.TEXT, new StepsTextAdapter());
        hourlyAdapters.put(Visualization.BAR_CHART, new StepsHourlyBarAdapter());

        HashMap<String, VisualizationAdapter> dailyAdapters = new HashMap<>();
        dailyAdapters.put(Visualization.TEXT, new StepsTextAdapter());
        dailyAdapters.put(Visualization.BAR_CHART, new StepsDailyBarAdapter());

        adapters.put(ATTRIBUTE_HOURLY, hourlyAdapters);
        adapters.put(ATTRIBUTE_DAILY, dailyAdapters);

//        getConfig().edit().clear().commit();
    }

    @Override
    public void limitNotification(SensorData data)
    {
        SharedPreferences prefs = getConfig();

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        DateFormat dateFormat = DateFormat.getDateInstance();
        String stringDate = dateFormat.format(date);

        if (prefs.getBoolean(stringDate, false))
            return;

        StepCounterData stepsData = (StepCounterData) data;
        if (stepsData.getNumSteps() >= limit.value)
        {
            NotificationCreator.create(resource, "Sense", "Well done, you've reached your daily steps goal!");

            prefs.edit().putBoolean(stringDate, true).commit();
        }
    }

    @Override
    public void correctData(SensorData data)
    {
        StepCounterData stepsData = (StepCounterData) data;
        float lastCount = stepsData.getNumSteps();

        if (firstCount == 0)
        {
            firstCount = lastCount;
            stepsData.setNumSteps(0);

            return;
        }

        steps = lastCount - firstCount;
        stepsData.setNumSteps(steps);
    }

    @Override
    public void purge()
    {
        steps = 0;
        firstCount = 0;

        super.purge();
    }
}

