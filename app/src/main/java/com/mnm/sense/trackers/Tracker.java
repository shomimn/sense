package com.mnm.sense.trackers;


import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.mnm.sense.Continuation;
import com.mnm.sense.PermissionHandler;
import com.mnm.sense.Repository;
import com.mnm.sense.SenseApp;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;

public class Tracker implements SensorDataListener
{
    public String text;
    public int resource;
    public boolean isOn;

    public int type;
    public int id = -1;
    public String[] permissions;

    protected PermissionHandler permissionHandler;

    public Tracker(PermissionHandler handler, int t) throws ESException
    {
        permissionHandler = handler;
        type = t;

//        sensorManager = ESSensorManager.getSensorManager(SenseApp.context());
//        id = sensorManager.subscribeToSensorData(type, this);
    }

    @Override
    public void onDataSensed(SensorData data)
    {
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

    public void start() throws ESException
    {
        id = sensorManager().subscribeToSensorData(type, this);
    }

    public void pause() throws ESException
    {
        ESSensorManager.getSensorManager(SenseApp.context()).pauseSubscription(id);
    }

    public void unpause() throws ESException
    {
        ESSensorManager.getSensorManager(SenseApp.context()).unPauseSubscription(id);
    }

    public void requestPermission()
    {
        permissionHandler.request(type, permissions);
    }

    public void onPermissionResponse(boolean granted) throws ESException
    {
        if (!granted)
//            id = ESSensorManager.getSensorManager(SenseApp.context()).subscribeToSensorData(type, this);
//        else
            Toast.makeText(SenseApp.context(), String.format("Permission denied for %s", text), Toast.LENGTH_SHORT).show();
    }

    public boolean subscribed()
    {
        return id > -1;
    }
}
