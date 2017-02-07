package com.ubhave.sensormanager.data.pull;

import android.util.SparseArray;

public class ActivityRecognitionData
{
    private static SparseArray<String> activityText;

    private int type;
    private int confidence;
    private long timestamp;

    public int getType()
    {
        return type;
    }

    public static SparseArray<String> getActivityMap()
    {
        return activityText;
    }

    public int getConfidence()
    {
        return confidence;
    }

    public long getTimestamp()
    {
        return timestamp;
    }
}
