package com.mnm.sense.trackers;


import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.BatteryLineAdapter;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.ArrayList;
import java.util.HashMap;

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

        build()
            .lineChart(new Visualization(2, 3, false))
            .attribute(ATTRIBUTE_PERCENT)
            .adapters(new BatteryLineAdapter());
    }
}

