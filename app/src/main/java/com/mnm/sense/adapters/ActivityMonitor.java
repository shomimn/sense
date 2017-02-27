package com.mnm.sense.adapters;


import android.util.SparseArray;

import com.google.android.gms.location.DetectedActivity;
import com.ubhave.sensormanager.data.pull.ActivityRecognitionData;
import com.ubhave.sensormanager.data.pull.ActivityRecognitionDataList;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ActivityMonitor
{
    public class ActivityData
    {
        public long activityTime;
        public long totalTime;

        public ActivityData(long activityTime, long totalTime)
        {
            this.activityTime = activityTime;
            this.totalTime = totalTime;
        }
    }

    private static final int CONFIDENCE_THRESHOLD = 30;

    public SparseArray<ActivityData> activityTimes;

    private long startTimestamp = 0;
    private int activityType = -1;

    public ActivityMonitor()
    {
        activityTimes = new SparseArray<>();
        activityTimes.append(DetectedActivity.WALKING, new ActivityData(0l, 0l));
        activityTimes.append(DetectedActivity.RUNNING, new ActivityData(0l, 0l));
        activityTimes.append(DetectedActivity.STILL, new ActivityData(0l, 0l));
        activityTimes.append(DetectedActivity.IN_VEHICLE, new ActivityData(0l, 0l));
        activityTimes.append(DetectedActivity.ON_BICYCLE, new ActivityData(0l, 0l));
    }

    public void resetTimes()
    {
        for (int i = 0; i < activityTimes.size(); ++i)
            activityTimes.put(activityTimes.keyAt(i), new ActivityData(0l, 0l));
    }

    private ActivityRecognitionData getReliableData(ActivityRecognitionData a1, ActivityRecognitionData a2)
    {
        if(a1 != null && a2 != null)
            return a1.getConfidence() > a2.getConfidence() ? a1 : a2;
        if(a1 != null)
            return a1;
        if(a2 != null)
            return a2;
        return null;
    }

    private SparseArray<ActivityRecognitionData> getValidActivity(ActivityRecognitionDataList activityList)
    {
        SparseArray<ActivityRecognitionData> result = new SparseArray<>();

        for(ActivityRecognitionData activity : activityList.getActivities())
            if(activity.getConfidence() >= CONFIDENCE_THRESHOLD)
                result.append(activity.getType(),activity);

        return result;
    }

    public int getTotalTimes(int... keys)
    {
        int res = 0;

        for(int key : keys)
            res += activityTimes.get(key).activityTime;
        return res;
    }

    public void subscribe(int ... keys)
    {

    }

    public void obtainTimes(ActivityRecognitionDataList dataList)
    {
        final SparseArray<ActivityRecognitionData> valid = getValidActivity(dataList);

        ActivityRecognitionData reliableData =
                getReliableData(valid.get(DetectedActivity.WALKING), valid.get(DetectedActivity.RUNNING));

        if(reliableData != null)
        {
            if(activityType == -1)
            {
                startTimestamp = reliableData.getTimestamp();
                activityType = reliableData.getType();
            }
            else
            {
                if(reliableData.getType() != activityType)
                {
                    long time = reliableData.getTimestamp() - startTimestamp;
                    activityTimes.put(activityType, activityTimes.get(activityType).activityTime + time);
                    activityType = reliableData.getType();
                    startTimestamp = reliableData.getTimestamp();
                }
                else
                {
                    long time = reliableData.getTimestamp() - startTimestamp;
                    activityTimes.put(activityType, activityTimes.get(activityType) + time);
                    startTimestamp = reliableData.getTimestamp();
                }
            }
        }
        else
        {
            if (activityType != -1)
            {
                long time = dataList.getActivities().get(0).getTimestamp() - startTimestamp;
                activityTimes.put(activityType, activityTimes.get(activityType) + time);
                activityType = -1;
                startTimestamp = 0;
            }
        }
    }

    public int getTimeMin(int key)
    {
        return (int) TimeUnit.MILLISECONDS.toMinutes(activityTimes.get(key));
    }
}
