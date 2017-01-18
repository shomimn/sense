package com.mnm.sense.trackers;

import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.push.ProximityData;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class ProximityTracker extends Tracker
{
    public ProximityTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_PROXIMITY);

        text = "Proximity";
        resource = R.drawable.ic_wifi_tethering_black_48dp;
        isOn = false;

        visualizations.put(Visualization.TEXT, new Visualization(1, 1, false));
    }
}

