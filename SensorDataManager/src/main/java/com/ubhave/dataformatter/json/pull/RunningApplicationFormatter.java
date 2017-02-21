package com.ubhave.dataformatter.json.pull;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.hardware.Sensor;
import android.util.Base64;
import android.util.Log;

import com.ubhave.dataformatter.json.PullSensorJSONFormatter;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.RunningApplicationData;
import com.ubhave.sensormanager.data.pull.RunningApplicationDataList;
import com.ubhave.sensormanager.process.AbstractProcessor;
import com.ubhave.sensormanager.process.pull.ActivityRecognitionProcessor;
import com.ubhave.sensormanager.process.pull.PhoneRadioProcessor;
import com.ubhave.sensormanager.process.pull.RunningApplicationProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class RunningApplicationFormatter extends PullSensorJSONFormatter
{
    private final static String RUNNING_APPS = "runningApplications";
    private final static String PACKAGE_NAME = "packageName";
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

                appInfo.put(PACKAGE_NAME, result.getPackageName());
                appInfo.put(NAME, result.getName());
                appInfo.put(FOREGROUND_TIME, result.getForegroundTime());
                appInfo.put(LAST_TIME_USED, result.getLastTimeUsed());
                resultJSON.put(appInfo);
            }
        }
        else
        {
            resultJSON.put(UNAVAILABLE);
        }

        Log.d("running app: ", resultJSON.toString(2));

        json.put(RUNNING_APPS, resultJSON);
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

            boolean setRawData = true;
            boolean setProcessedData = true;

            try
            {
                List<RunningApplicationData> dataList = new ArrayList<>();

                JSONArray jsonArray = (JSONArray) jsonData.get(RUNNING_APPS);

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject entry = jsonArray.getJSONObject(i);
                    String packageName = entry.getString(PACKAGE_NAME);
                    String name = entry.getString(NAME);
                    long ft = entry.getLong(FOREGROUND_TIME);
                    long ltu = entry.getLong(LAST_TIME_USED);
                    dataList.add(new RunningApplicationData(packageName, name, ft, null, ltu));
                }
                RunningApplicationProcessor processor = (RunningApplicationProcessor) AbstractProcessor.getProcessor(applicationContext, sensorType, setRawData, setProcessedData);
                return processor.process(senseStartTimestamp, dataList, sensorConfig);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getStringFromDrawable(Drawable d)
    {
        Bitmap bitmap = getBitmapFromDrawable(d);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

    private Drawable getDrawableFromString(String s)
    {
        byte[] stream = Base64.decode(s, Base64.DEFAULT);
        return  new BitmapDrawable(BitmapFactory.decodeByteArray(stream, 0, stream.length));
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable)
    {
        try {
            Bitmap bitmap;

            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            // Handle the error
            return null;
        }
    }
}
