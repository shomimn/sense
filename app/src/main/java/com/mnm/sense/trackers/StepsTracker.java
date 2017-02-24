package com.mnm.sense.trackers;

import android.content.SharedPreferences;

import com.mnm.sense.NotificationCreator;
import com.mnm.sense.R;
import com.mnm.sense.Timestamp;
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

    float prevCount = 0;
    float steps = 0;
    float totalSteps = 0;

    public StepsTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_STEP_COUNTER);

        text = "Steps";
        resource = R.drawable.ic_directions_walk;
        isOn = false;

        limit = new Limit("Daily goal", 1000, 100, 20000);

        attributes = new String[] { ATTRIBUTE_HOURLY };

        build()
            .text(new Visualization(1, 1, false))
            .barChart(new Visualization(1, 3, false))
            .attribute(ATTRIBUTE_HOURLY)
            .adapters(new StepsTextAdapter(),
                    new StepsHourlyBarAdapter());

//        getConfig().edit().clear().commit();
    }

    @Override
    public void limitNotification(SensorData data)
    {
        SharedPreferences prefs = getConfig();

        String date = Timestamp.now().date();

        if (prefs.getBoolean(date, false))
            return;

        if (totalSteps >= limit.value)
        {
            NotificationCreator.create(resource, "Sense", "Well done, you've reached your daily steps goal!");

            prefs.edit().putBoolean(date, true).commit();
        }
    }

    @Override
    public void correctData(SensorData data)
    {
        StepCounterData stepsData = (StepCounterData) data;
        float count = stepsData.getNumSteps();

        if (prevCount == 0)
            prevCount = count;

        steps = count - prevCount;
        stepsData.setNumSteps(steps);

        totalSteps += steps;

        prevCount = count;
    }

    @Override
    public void purge()
    {
        totalSteps = 0;
        steps = 0;
        prevCount = 0;

        super.purge();
    }
}

