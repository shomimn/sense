package com.mnm.sense.trackers;

import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.mnm.sense.adapters.WifiBarAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.HashMap;

public class WifiTracker extends Tracker
{
    public static final String ATTRIBUTE_LEVEL = "Level";

    public WifiTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_WIFI);

        text = "WiFi";
        resource = R.drawable.ic_wifi_black_48dp;
        isOn = false;

        accent = R.color.redColorAccent;
        theme = R.style.RedTheme;

        attributes = new String[] { ATTRIBUTE_LEVEL };

        visualizations.put(Visualization.BAR_CHART, new Visualization(1, 3, false));

        HashMap<String, VisualizationAdapter> levelAdapters = new HashMap<>();
        levelAdapters.put(Visualization.BAR_CHART, new WifiBarAdapter());

        adapters.put(ATTRIBUTE_LEVEL, levelAdapters);
    }
}

