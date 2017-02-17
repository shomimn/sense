package com.ubhave.sensormanager.sensors.pull;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.config.pull.PullSensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.ActivityRecognitionDataList;
import com.ubhave.sensormanager.process.pull.ActivityRecognitionProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.sensormanager.sensors.push.AbstractPushSensor;

import java.util.List;

public class ActivityRecognitionSensor extends AbstractPullSensor implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{


    private static final String TAG = "ActivityRecognitionSensor";
    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.PACKAGE_USAGE_STATS
    };

    private GoogleApiClient mApiClient;
    private PendingIntent pendingIntent;

    private static ActivityRecognitionSensor activityRecognitionSensor;
    private static Object lock = new Object();

    List<DetectedActivity> detectedActivities;
    private ActivityRecognitionDataList activities;

    public static ActivityRecognitionSensor getSensor(final Context context) throws ESException
    {
        if (activityRecognitionSensor == null)
        {
            synchronized (lock)
            {
                if (activityRecognitionSensor == null)
                {
                    activityRecognitionSensor = new ActivityRecognitionSensor(context);
                }
            }
        }
        return activityRecognitionSensor;
    }

    public static ActivityRecognitionSensor getSensor()
    {
        return activityRecognitionSensor;
    }

    protected ActivityRecognitionSensor(Context context)
    {
        super(context);
        mApiClient = new GoogleApiClient.Builder(context)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();

    }

    @Override
    protected SensorData getMostRecentRawData()
    {
        Log.d("stigloo", "Ddsada");
        return activities;

    }

    @Override
    protected void processSensorData()
    {
        ActivityRecognitionProcessor processor = (ActivityRecognitionProcessor) getProcessor();
        activities = processor.process(pullSenseStartTimestamp, detectedActivities, sensorConfig.clone());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
//        new Thread()
//        {
//            @Override
//            public void run()
//            {
////                try
////                {
//                    Intent intent = new Intent(applicationContext, ActivityRecognizedService.class);
//                    PendingIntent pendingIntent = PendingIntent.getService(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                    ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, 0, pendingIntent);
////                }
////                catch (ESException e)
////                {
////                    e.printStackTrace();
////                }
//            }
//        }.start();

    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.d("Connection suspend", String.valueOf(i));

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Log.d("Connection failed", connectionResult.toString());
    }


    @Override
    protected boolean startSensing()
    {
        if (mApiClient != null)
        {
            Intent intent = new Intent(applicationContext, ActivityRecognizedService.class);
            pendingIntent = PendingIntent.getService(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, 0, pendingIntent);

            return true;
        }
        return false;
    }

    @Override
    protected void stopSensing()
    {
//        mApiClient.disconnect();
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mApiClient, pendingIntent);
    }

    @Override
    protected String getLogTag()
    {
        return TAG;
    }

    @Override
    public int getSensorType()
    {
        return SensorUtils.SENSOR_TYPE_ACTIVITY_RECOGNITION;
    }

    public void handleDetectedActivities(List<DetectedActivity> probableActivities)
    {
        detectedActivities = probableActivities;
        notifySenseCyclesComplete();

    }
}