package com.mnm.sense.trackers;


import com.mnm.sense.Repository;
import com.mnm.sense.SenseApp;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.config.pull.PullSensorConfig;
import com.ubhave.sensormanager.data.SensorData;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Tracker implements SensorDataListener
{
    public interface UpdateCallback
    {
        void update(ArrayList<SensorData> with);
    }

    public String text;
    public int resource;
    public boolean isOn;

    public int type;
    public int id = -1;

    public HashMap<String, Visualization> visualizations = new HashMap<>();
    public HashMap<String, VisualizationAdapter> adapters = new HashMap<>();
    public HashMap<String, UpdateCallback> updateCallbacks = new HashMap<>();
    public ArrayList<SensorData> sensorData = new ArrayList<>();

    public Tracker(int t) throws ESException
    {
        type = t;
    }

    @Override
    public void onDataSensed(SensorData data)
    {
        sensorData.add(data);
        updateViews();

        try
        {
            Repository.instance().logSensorData(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
    {

    }

    // Avoid instantiating the sensor manager before getting all required permissions
    protected ESSensorManager sensorManager() throws ESException
    {
        return ESSensorManager.getSensorManager(SenseApp.context());
    }

    public void start() throws ESException
    {
        id = sensorManager().subscribeToSensorData(type, this);
    }

    public void restart()
    {
        try
        {
            if (subscribed())
            {
                sensorManager().unsubscribeFromSensorData(id);
                start();
            }
        }
        catch (ESException e)
        {
            e.printStackTrace();
        }
    }

    public void pause() throws ESException
    {
        sensorManager().pauseSubscription(id);
    }

    public void unpause() throws ESException
    {
        sensorManager().unPauseSubscription(id);
    }

    public boolean subscribed()
    {
        return id != -1;
    }

    public long getSenseInterval()
    {
        long senseInterval = 0;

        try
        {
            senseInterval = (long) sensorManager().getSensorConfigValue(type, PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS);
        }
        catch (ESException e)
        {
            e.printStackTrace();
        }

        return senseInterval / 1000;
    }

    public void setSenseInterval(long interval)
    {
        try
        {
            ESSensorManager manager = sensorManager();
            Object value = manager.getSensorConfigValue(type, PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS);

            if (value != null)
                manager.setSensorConfig(type, PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, interval);
        }
        catch (ESException e)
        {
            e.printStackTrace();
        }
    }

    public Object getModel(String visualizationType)
    {
        if (sensorData.size() > 0)
            return adapters.get(visualizationType).adapt(sensorData);

        return null;
    }

    public void updateViews()
    {
        for (HashMap.Entry entry : updateCallbacks.entrySet())
        {
            String key = (String) entry.getKey();
            UpdateCallback callback = (UpdateCallback) entry.getValue();

            callback.update(sensorData);
        }
    }
}
