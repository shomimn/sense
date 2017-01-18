package com.mnm.sense.trackers;


import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.push.BatteryData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class BatteryTracker extends Tracker
{
    public BatteryTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_BATTERY);

        text = "Battery";
        resource = R.drawable.ic_battery_full_black_48dp;
        isOn = false;

        visualizations.put(Visualization.BAR_CHART, new Visualization(1, 3, false));
        visualizations.put(Visualization.PIE_CHART, new Visualization(1, 3, false));
    }
}

