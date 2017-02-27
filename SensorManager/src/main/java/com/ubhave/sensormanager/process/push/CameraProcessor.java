package com.ubhave.sensormanager.process.push;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Camera;
import android.util.Log;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.push.CameraData;
import com.ubhave.sensormanager.process.AbstractProcessor;

public class CameraProcessor extends AbstractProcessor
{
    public CameraProcessor(Context context, boolean rw, boolean sp)
    {
        super(context, rw, sp);
    }

    public CameraData process(long recvTime, SensorConfig config, Intent dataIntent)
    {
        CameraData data = new CameraData(recvTime, config);

        try
        {
            Cursor cursor = appContext.getContentResolver().query(dataIntent.getData(), null,null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex("_data"));

            data.setImagePath(imagePath);
        }
        catch(Exception e)
        {
            Log.d("CameraEventReceiver: ", "New Photo without image path");
            e.printStackTrace();
        }

        return data;
    }
}
