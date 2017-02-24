package com.ubhave.sensormanager.process.pull;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.pull.RunningApplicationData;
import com.ubhave.sensormanager.data.pull.RunningApplicationDataList;
import com.ubhave.sensormanager.process.AbstractProcessor;

import java.util.ArrayList;
import java.util.List;


public class RunningApplicationProcessor extends AbstractProcessor
{
    private PackageManager packageManager;

    public RunningApplicationProcessor(Context context, boolean rw, boolean sp)
    {
        super(context, rw, sp);
        packageManager = context.getPackageManager();
    }

    @TargetApi(21)
    public <T> RunningApplicationDataList process(long pullSenseStartTimestamp, List<T> runningAppsData, SensorConfig sensorConfig)
    {
        RunningApplicationDataList runningApplicationDataList = new RunningApplicationDataList(pullSenseStartTimestamp, sensorConfig);
        if(runningAppsData.size() == 0)
            return null;
        if(setProcessedData)
        {
            ArrayList<RunningApplicationData> runningAppsDataList = (ArrayList<RunningApplicationData>)runningAppsData;
            for(RunningApplicationData data : runningAppsDataList)
            {
                String packageName = data.getPackageName();
                ApplicationInfo appInfo = getApplicationInfo(packageName);

                if (appInfo != null)
                {
                    String name = (String) packageManager.getApplicationLabel(appInfo);
                    Drawable icon = packageManager.getApplicationIcon(appInfo);

                    data.setIcon(icon);
                }
            }
            runningApplicationDataList.setRunningApplications(runningAppsDataList);
        }
        else
        {
            ArrayList<UsageStats> runningAppsUsageStats = (ArrayList<UsageStats>)runningAppsData;
            for(UsageStats stats : runningAppsUsageStats)
            {
                String packageName = stats.getPackageName();
                ApplicationInfo appInfo = getApplicationInfo(packageName);

                if (appInfo != null)
                {
                    long ft = stats.getTotalTimeInForeground();

                    if (ft == 0)
                        continue;

                    long ltu = stats.getLastTimeUsed();
                    String name = (String) packageManager.getApplicationLabel(appInfo);
                    Drawable icon = packageManager.getApplicationIcon(appInfo);

                    RunningApplicationData appData = new RunningApplicationData(packageName, name, ft, icon, ltu);

                    runningApplicationDataList.addRunningApplication(appData);
                }
            }
        }

        return runningApplicationDataList;
    }

    private ApplicationInfo getApplicationInfo(String packageName)
    {
        ApplicationInfo appInfo;
        try
        {
            appInfo = packageManager.getApplicationInfo(packageName, 0);
        }
        catch(final PackageManager.NameNotFoundException e)
        {
            appInfo = null;
        }
        return appInfo;
    }

}
