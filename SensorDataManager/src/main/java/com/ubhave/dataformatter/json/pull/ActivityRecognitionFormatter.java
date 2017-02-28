package com.ubhave.dataformatter.json.pull;


import android.content.Context;
import android.util.Log;

import com.ubhave.dataformatter.json.PullSensorJSONFormatter;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.ActivityRecognitionData;
import com.ubhave.sensormanager.data.pull.ActivityRecognitionDataList;
import com.ubhave.sensormanager.data.pull.RunningApplicationData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityRecognitionFormatter extends PullSensorJSONFormatter
{
    private final static String UNAVAILABLE = "unavailable";
    private final static String ACTIVITY_RECOGNITION = "ActivityRecognition";
    private final static String TYPE = "type";
    private final static String CONFIDENCE = "confidence";

    public ActivityRecognitionFormatter(Context context)
    {
        super(context, SensorUtils.SENSOR_TYPE_ACTIVITY_RECOGNITION);
    }

    @Override
    protected void addSensorSpecificData(JSONObject json, SensorData data) throws JSONException, DataHandlerException
    {
        ArrayList<ActivityRecognitionData> results = ((ActivityRecognitionDataList) data).getActivities();

        JSONArray resultJson = new JSONArray();

        if(results != null)
        {
            for(ActivityRecognitionData activity : results)
            {
                JSONObject activityJson = new JSONObject();
                activityJson.put(TYPE, activity.getType());
                activityJson.put(CONFIDENCE, activity.getConfidence());

                resultJson.put(activityJson);
            }
        }
        else
        {
            resultJson.put(UNAVAILABLE);
        }
        Log.d("activity: ", resultJson.toString(2));
        json.put(ACTIVITY_RECOGNITION, resultJson);
    }

    @Override
    protected void addSensorSpecificConfig(JSONObject json, SensorConfig config) throws JSONException
    {

    }

    @Override
    public SensorData toSensorData(String dataString)
    {
        JSONObject jsonData = super.parseData(dataString);

        if(jsonData != null)
        {
            long senseStartTimestamp = super.parseTimeStamp(jsonData);
            SensorConfig sensorConfig = super.getGenericConfig(jsonData);

            try
            {
                ArrayList<ActivityRecognitionData> dataList = new ArrayList<>();
                JSONArray jsonArray = jsonData.getJSONArray(ACTIVITY_RECOGNITION);
                Log.d("json: " , jsonArray.toString(1));
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject entry = jsonArray.getJSONObject(i);
                    int type = entry.getInt(TYPE);
                    int confidence = entry.getInt(CONFIDENCE);

                    dataList.add(new ActivityRecognitionData(type, confidence));
                }

                ActivityRecognitionDataList activityRecognitionDataList = new ActivityRecognitionDataList(senseStartTimestamp, sensorConfig);
                activityRecognitionDataList.setActivities(dataList);
                return activityRecognitionDataList;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }
}
