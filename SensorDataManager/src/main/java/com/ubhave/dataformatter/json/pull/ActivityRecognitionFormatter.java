package com.ubhave.dataformatter.json.pull;


import android.content.Context;

import com.ubhave.dataformatter.json.PullSensorJSONFormatter;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityRecognitionFormatter extends PullSensorJSONFormatter
{
    private final static String ACTIVITY_RECOGNITION = "ActivityRecognition";
    private final static String ACTIVITY_TEXT = "activityText";
    private final static String TYPE = "type";
    private final static String CONFIDENCE = "confidence";

    public ActivityRecognitionFormatter(Context context)
    {
        super(context, SensorUtils.SENSOR_TYPE_ACTIVITY_RECOGNITION);
    }

    @Override
    protected void addSensorSpecificData(JSONObject json, SensorData data) throws JSONException, DataHandlerException
    {

    }

    @Override
    protected void addSensorSpecificConfig(JSONObject json, SensorConfig config) throws JSONException
    {

    }

    @Override
    public SensorData toSensorData(String dataString)
    {
        return null;
    }
}
