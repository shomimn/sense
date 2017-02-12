package com.mnm.sense.trackers;

import android.widget.TextView;

import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.ActivityRecognitionData;
import com.ubhave.sensormanager.data.pull.ActivityRecognitionDataList;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.ArrayList;
import java.util.HashMap;


public class ActivityTracker extends Tracker
{
    public static final String ATTRIBUTE_TIME = "Time";

    public ActivityTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_ACTIVITY_RECOGNITION);

        text = "Activity";
        resource = R.drawable.ic_arrowhead_black_48dp;
        isOn = false;

        attributes = new String[] { "Time" };

        visualizations.put(Visualization.TEXT, new Visualization(1, 3, false));

        HashMap<String, VisualizationAdapter> timeAdapters = new HashMap<>();
        timeAdapters.put(Visualization.TEXT, new ActivityTextAdapter());

        adapters.put(ATTRIBUTE_TIME, timeAdapters);
    }
}

class ActivityTextAdapter implements VisualizationAdapter<TextView, String>
{
    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        return merge(adaptAll(data));
    }

    @Override
    public String adaptOne(SensorData data)
    {
        return null;
    }

    @Override
    public ArrayList<String> adaptAll(ArrayList<SensorData> data)
    {
        ArrayList<String> result = new ArrayList<>();

        for (SensorData sensorData : data)
        {
        }

        return result;
    }

    @Override
    public void prepareView(TextView view)
    {

    }

    private String merge(ArrayList<String> data)
    {
        String result = "";

        for (String s : data)
            result += s + "\n";

        return result;
    }
}