package com.mnm.sense;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.mnm.sense.trackers.Tracker;
import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Task
{
    public static final int TOTAL = 2;

    public static final int SERVER = 0;
    public static final int GENERAL = 1;

    public int type;

    public Task()
    {
    }

    public Task(int t)
    {
        type = t;
    }

    public abstract void execute();

    public static abstract class Ui extends Task
    {
        public Ui()
        {
        }

        public Ui(int t)
        {
            super(t);
        }

        public abstract void uiExecute();
    }

    public static abstract class Progress extends Ui
    {
        ProgressDialog dialog;
        String url;

        public Progress(Context context, String _url, String message)
        {
            super(SERVER);

            url = _url;

            dialog = new ProgressDialog(context);
            dialog.setMessage(message);
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);

            dialog.show();
        }

        @Override
        public void execute()
        {
            Log.d("Progress Task", "Downloading data from server");

            try
            {
                URL httpUrl = new URL(url);

                HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
                connection.setFixedLengthStreamingMode(5 * 1024);

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK)
                {
                    StringBuilder builder = new StringBuilder();

                    BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                    String line;

                    while ((line = reader.readLine()) != null)
                        builder.append(line);

                    inputStream.close();

                    HashMap<Integer, ArrayList<SensorData>> dataByTracker = new HashMap<>();
                    JSONArray jsonArray = new JSONArray(builder.toString());

                    for (int i = 0; i < jsonArray.length(); ++i)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String type = object.getString("dataType");
                        int tracker = SensorUtils.getSensorType(type);
                        JSONFormatter formatter = DataFormatter.getJSONFormatter(dialog.getContext(), tracker);

                        SensorData data = formatter.toSensorData(object.toString());

                        Log.d("Progress Task", "Got sensor data");

                        if (dataByTracker.get(tracker) == null)
                            dataByTracker.put(tracker, new ArrayList<SensorData>());

                        dataByTracker.get(tracker).add(data);
                    }

                    for (Integer trackerType : dataByTracker.keySet())
                    {
                        Tracker tracker = SenseApp.instance().tracker(trackerType);
                        tracker.remoteData = dataByTracker.get(trackerType);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void uiExecute()
        {
            executeImpl();

            dialog.dismiss();
        }

        public abstract void executeImpl();
    }
}
