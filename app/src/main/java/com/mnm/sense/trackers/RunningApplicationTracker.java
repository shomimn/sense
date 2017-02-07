package com.mnm.sense.trackers;

import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.RunningApplicationTextAdapter;
import com.mnm.sense.models.TextModel;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class RunningApplicationTracker extends Tracker
{
    public RunningApplicationTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_RUNNING_APP);
        text = "Running Apps";
        resource = R.drawable.ic_dashboard_black_48dp;
        isOn = false;

        visualizations.put(Visualization.TEXT, new Visualization(3, 3, false));
        adapters.put(Visualization.TEXT, new RunningApplicationTextAdapter());

    }

    @Override
    public Object getModel(String visualizationType)
    {
        if(visualizationType.equals(Visualization.TEXT))
            return new TextModel(this, (String) super.getModel(visualizationType));
        return null;
    }

}
