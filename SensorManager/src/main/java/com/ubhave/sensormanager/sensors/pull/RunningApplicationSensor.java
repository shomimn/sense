package com.ubhave.sensormanager.sensors.pull;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.pull.RunningApplicationDataList;
import com.ubhave.sensormanager.process.pull.RunningApplicationProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;
import java.util.Calendar;
import java.util.List;


public class RunningApplicationSensor extends AbstractPullSensor
{
    private static final String TAG = "RunningApplicationSensor";

    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.PACKAGE_USAGE_STATS
    };

    private static RunningApplicationSensor runningApplicationSensor;
    private UsageStatsManager usageStatsManager;
    private static Object lock = new Object();

    private List<UsageStats> runningAppsUsageStats;
    private RunningApplicationDataList runningApplicationDataList;


    public static RunningApplicationSensor getSensor(final Context context) throws ESException
    {
        if (runningApplicationSensor == null)
        {
            synchronized (lock)
            {
                if (runningApplicationSensor == null)
                {
                    runningApplicationSensor = new RunningApplicationSensor(context);
                }
            }
        }
        return runningApplicationSensor;
    }

    @TargetApi(22)
    private RunningApplicationSensor(Context context)
    {
        super(context);
        usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
    }

    @Override
    public RunningApplicationDataList getMostRecentRawData()
    {
        return runningApplicationDataList;
    }

    @Override
    public void processSensorData()
    {
        RunningApplicationProcessor processor = (RunningApplicationProcessor) getProcessor();
        runningApplicationDataList = processor.process(pullSenseStartTimestamp, runningAppsUsageStats, sensorConfig.clone());
    }

    @Override
    public boolean startSensing()
    {
        new Thread()
        {
            @SuppressLint("NewApi")
            public void run()
            {
                try
                {
                    synchronized (RunningApplicationSensor.this)
                    {
                        runningAppsUsageStats = null;

                        runningAppsUsageStats = getUsageStatsList();
                        RunningApplicationSensor.this.notify();
                    }
                }
                catch (Exception exp)
                {
                    exp.printStackTrace();
                }
                finally
                {
                    notifySenseCyclesComplete();
                }

            }
        }.start();
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
        return SensorUtils.SENSOR_TYPE_RUNNING_APP;
    }

    @TargetApi(21)
    public List<UsageStats> getUsageStatsList(){
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
//        calendar.add(Calendar.DATE, -1);
//        calendar.add(Calendar.HOUR_OF_DAY, -1);
        calendar.add(Calendar.MINUTE, -1);

        long startTime = calendar.getTimeInMillis();

        List<UsageStats> list = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);


        return list;
    }
}

