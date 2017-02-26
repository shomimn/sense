package com.ubhave.dataformatter.json.push;

import com.ubhave.dataformatter.json.PushSensorJSONFormatter;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.push.CameraData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Pair;

public class CameraFormatter extends PushSensorJSONFormatter
{
    private final static String IMAGE_PATH = "imagePath";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    public CameraFormatter(Context context)
    {
        super(context, SensorUtils.SENSOR_TYPE_CAMERA);
    }

    @Override
    protected void addSensorSpecificData(JSONObject json, SensorData data) throws JSONException, DataHandlerException
    {
        CameraData cameraData = (CameraData)data;
        json.put(IMAGE_PATH, cameraData.getImagePath());
        Pair<Double, Double> location = cameraData.getLocation();

        if (location != null)
        {
            json.put(LATITUDE, location.first);
            json.put(LONGITUDE, location.second);
        }
    }

    @Override
    public SensorData toSensorData(String dataString)
    {
        JSONObject jsonData = parseData(dataString);
        if (jsonData != null)
        {
            long timestamp = parseTimeStamp(jsonData);
            SensorConfig sensorConfig = getGenericConfig(jsonData);
            CameraData cameraData = new CameraData(timestamp, sensorConfig);
            try
            {
                cameraData.setImagePath(jsonData.getString(IMAGE_PATH));
                Pair<Double, Double> location = null;

                if (jsonData.has(LATITUDE) && jsonData.has(LONGITUDE))
                {
                    location = Pair.create(
                            jsonData.getDouble(LATITUDE),
                            jsonData.getDouble(LONGITUDE));

                }
                cameraData.setLocation(location);

                return cameraData;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return cameraData;
        }
        else
        {
            return null;
        }

    }
}
