package com.ubhave.sensormanager.sensors.pull;

import android.Manifest;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.push.AbstractPushSensor;

import java.util.List;

public class ActivityRecognitionSensor extends AbstractPullSensor implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{


    private static final String TAG = "ActivityRecognitionSensor";
    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.PACKAGE_USAGE_STATS
    };

    private GoogleApiClient mApiClient;

    private static ActivityRecognitionSensor activityRecognitionSensor;
    private static Object lock = new Object();

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

    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent( applicationContext, ActivityRecognizedService.class );
                PendingIntent pendingIntent = PendingIntent.getService( applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
                ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mApiClient, 0, pendingIntent );
            }
        }.start();

    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.d("Connection suspend", String.valueOf(i)) ;

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Log.d("Connection failed", connectionResult.toString());
    }

    @Override
    protected SensorData getMostRecentRawData()
    {
        return null;
    }

    @Override
    protected void processSensorData()
    {

    }

    @Override
    protected boolean startSensing()
    {
        return false;
    }

    @Override
    protected void stopSensing()
    {
        mApiClient.disconnect();
    }

    @Override
    protected String getLogTag()
    {
        return TAG;
    }

    @Override
    public int getSensorType()
    {
        return 0;
    }

    public void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        for( DetectedActivity activity : probableActivities ) {
            switch( activity.getType() ) {
                case DetectedActivity.IN_VEHICLE: {
                    Log.e( "ActivityRecogition", "In Vehicle: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    Log.e( "ActivityRecogition", "On Bicycle: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    Log.e( "ActivityRecogition", "On Foot: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.e( "ActivityRecogition", "Running: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.STILL: {
                    Log.e( "ActivityRecogition", "Still: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.TILTING: {
                    Log.e( "ActivityRecogition", "Tilting: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.WALKING: {
                    Log.e( "ActivityRecogition", "Walking: " + activity.getConfidence() );
//                    if( activity.getConfidence() >= 75 ) {
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//                        builder.setContentText( "Are you walking?" );
//                        builder.setSmallIcon( R.mipmap.ic_launcher );
//                        builder.setContentTitle( getString( R.string.app_name ) );
//                        NotificationManagerCompat.from(this).notify(0, builder.build());
//                    }
                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    Log.e( "ActivityRecogition", "Unknown: " + activity.getConfidence() );
                    break;
                }
            }
        }
    }
}
