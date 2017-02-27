package com.mnm.sense.adapters;


import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.location.DetectedActivity;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.ActivityRecognitionData;
import com.ubhave.sensormanager.data.pull.ActivityRecognitionDataList;
import com.ubhave.sensormanager.data.pull.RunningApplicationData;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

public class ActivityMonitor
{
    public class ActivityTimeTracker
    {
        public long startTimestamp;
        public int type;
        public SparseArray<Long> activityTimes;

        public ActivityTimeTracker()
        {
            startTimestamp = 0;
            type = -1;
            activityTimes = new SparseArray<>();

            activityTimes.append(DetectedActivity.WALKING, 0l);
            activityTimes.append(DetectedActivity.RUNNING, 0l);
            activityTimes.append(DetectedActivity.STILL, 0l);
            activityTimes.append(DetectedActivity.IN_VEHICLE, 0l);
            activityTimes.append(DetectedActivity.ON_BICYCLE, 0l);
        }

        public int getMinutes(int ... keys)
        {
            long res = 0;
            for(int key : keys)
                res += activityTimes.get(key);

            return (int) TimeUnit.MILLISECONDS.toMinutes(res);
        }
        public void resetTimes()
        {
            for (int i = 0; i < activityTimes.size(); ++i)
                activityTimes.put(activityTimes.keyAt(i), 0l);
        }

        public void obtainTimes(ActivityRecognitionDataList dataList)
        {
            ActivityRecognitionData reliableData = null;

            ArrayList<ActivityRecognitionData> valid = getValidActivity(dataList);
            if(valid.size() != 0)
                reliableData = valid.get(0);

            if(reliableData != null)
            {
                if(type == -1)
                {
                    startTimestamp = reliableData.getTimestamp();
                    type = reliableData.getType();
                }
                else
                {
                    if(reliableData.getType() != type)
                    {
                        long time = reliableData.getTimestamp() - startTimestamp;
                        activityTimes.put(type, activityTimes.get(type) + time);
                        type = reliableData.getType();
                        startTimestamp = reliableData.getTimestamp();
                    }
                    else
                    {
                        long time = reliableData.getTimestamp() - startTimestamp;
                        activityTimes.put(type, activityTimes.get(type) + time);
                        startTimestamp = reliableData.getTimestamp();
                    }
                }
            }
            else
            {
                if (type != -1)
                {
                    long time = dataList.getTimestamp() - startTimestamp;
                    activityTimes.put(type, activityTimes.get(type) + time);
                    type = -1;
                    startTimestamp = 0l;
                }
            }
        }
    }

    private static final int CONFIDENCE_THRESHOLD = 30;

    private ActivityTimeTracker liveTimeTracker;

    public ActivityMonitor()
    {
        liveTimeTracker = new ActivityTimeTracker();
    }
    
    private ArrayList<ActivityRecognitionData> getValidActivity(ActivityRecognitionDataList activityList)
    {
        ArrayList<ActivityRecognitionData> result = new ArrayList<>();

        for(ActivityRecognitionData activity : activityList.getActivities())
            if(liveTimeTracker.activityTimes.get(activity.getType(), -1l) != -1 && activity.getConfidence() >= CONFIDENCE_THRESHOLD)
                result.add(activity);

        return result;
    }

    private boolean isValid(ActivityRecognitionData data)
    {
        return data.getConfidence() >= CONFIDENCE_THRESHOLD;
    }

    public void liveMonitoring(SensorData dataList)
    {
        liveTimeTracker.obtainTimes((ActivityRecognitionDataList)dataList);
    }

    public ActivityTimeTracker monitorPortion(ArrayList<SensorData> dataList)
    {
        ActivityTimeTracker timeTracker = new ActivityTimeTracker();

        for(SensorData data: dataList)
            liveTimeTracker.obtainTimes((ActivityRecognitionDataList)data);

        return timeTracker;
    }

    public ActivityTimeTracker getLiveTracker()
    {
        return liveTimeTracker;
    }

}
