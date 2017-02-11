package com.ubhave.dataformatter.json.pull;

import android.content.Context;
import android.hardware.Sensor;

import com.ubhave.dataformatter.json.PullSensorJSONFormatter;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.RunningApplicationData;
import com.ubhave.sensormanager.data.pull.RunningApplicationDataList;
import com.ubhave.sensormanager.sensors.SensorUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RunningApplicationFormatter extends PullSensorJSONFormatter
{
    private final static String RUNNING_APPS = "runningApplications";
    private final static String NAME = "name";
    private final static String FOREGROUND_TIME = "foregroundTime";
    private final static String ICON = "icon";
    private final static String LAST_TIME_USED = "lastTimeUsed";
    private final static String BEGINNING_TIME_RANGE = "beginningTimeRange";
    private final static String END_TIME_RANGE = "endTimeRange";

    private final static String UNAVAILABLE = "unavailable";


    public RunningApplicationFormatter(Context context)
    {
        super(context, SensorUtils.SENSOR_TYPE_RUNNING_APP);
    }

    @Override
    protected void addSensorSpecificData(JSONObject json, SensorData data) throws JSONException, DataHandlerException
    {
        RunningApplicationDataList dataList = (RunningApplicationDataList) data;
        ArrayList<RunningApplicationData> results = dataList.getRunningApplications();

        JSONArray resultJSON = new JSONArray();

        if(results != null)
        {
            for(RunningApplicationData result : results)
            {
                JSONObject appInfo = new JSONObject();
                appInfo.put(NAME, result.getName());
                appInfo.put(FOREGROUND_TIME, result.getForegroundTime());
//                appInfo.put(ICON, result.getIcon().)
                appInfo.put(LAST_TIME_USED, result.getLastTimeUsed());
                appInfo.put(BEGINNING_TIME_RANGE, result.getBeginningTimeRange());
                appInfo.put(END_TIME_RANGE, result.getEndTimeRange());
                resultJSON.put(appInfo);
            }
        }
        else
        {
            resultJSON.put(UNAVAILABLE);
        }
        json.put(RUNNING_APPS, resultJSON);
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
