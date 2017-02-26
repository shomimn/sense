package com.ubhave.sensormanager.sensors.push;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.hardware.Camera;
import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.push.CameraData;
import com.ubhave.sensormanager.process.push.CameraProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;


public class CameraSensor extends AbstractPushSensor
{
    private static final String TAG = "CameraSensor";
    private static CameraSensor cameraSensor;
    private static Object lock = new Object();

    public CameraSensor(Context context)
    {
        super(context);
    }

    public static CameraSensor getSensor(final Context context) throws ESException
    {
        if (cameraSensor == null)
        {
            synchronized (lock)
            {
                if (cameraSensor == null)
                {
                    if (permissionGranted(context, Manifest.permission.CAMERA))
                    {
                        cameraSensor = new CameraSensor(context);
                    }
                    else
                    {
                        throw new ESException(ESException. PERMISSION_DENIED, SensorUtils.SENSOR_NAME_CAMERA);
                    }
                }
            }
        }
        return cameraSensor;
    }

    @Override
    protected void onBroadcastReceived(Context context, Intent intent)
    {
        CameraProcessor processor = (CameraProcessor)getProcessor();
        CameraData data = processor.process(System.currentTimeMillis(), sensorConfig.clone(), intent);
        onDataSensed(data);
    }

    @Override
    protected IntentFilter[] getIntentFilters()
    {
        IntentFilter[] filters = new IntentFilter[1];
        filters[0] = new IntentFilter(Camera.ACTION_NEW_PICTURE);
        try
        {
            filters[0].addDataType("image/*");
        }
        catch (IntentFilter.MalformedMimeTypeException e)
        {
            e.printStackTrace();
        }
        return filters;
    }

    @Override
    public boolean startSensing()
    {
        return true;
    }

    @Override
    public void stopSensing()
    {

    }

    @Override
    protected String getLogTag()
    {
        return TAG;
    }

    @Override
    public int getSensorType()
    {
        return SensorUtils.SENSOR_TYPE_CAMERA;
    }
}
