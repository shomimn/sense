package com.mnm.sense.adapters;


import android.util.Log;
import android.util.SparseArray;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.location.DetectedActivity;
import com.mnm.sense.Colors;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.ActivityRecognitionData;
import com.ubhave.sensormanager.data.pull.ActivityRecognitionDataList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ActivityPieAdapter extends VisualizationAdapter<PieChart, PieData>
{
    private static final int CONFIDENCE_THRESHOLD = 30;
    private int activityGoal = 120;
    private SparseArray<Long> activityTimes;
    private long startTimestamp = 0;
    private int activityType = -1;

    private int totalTime = 0;

    private boolean readFromRepository = false;

    public ActivityPieAdapter()
    {
        activityTimes = new SparseArray<>();
        activityTimes.append(DetectedActivity.WALKING, 0l);
        activityTimes.append(DetectedActivity.RUNNING, 0l);
    }

    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if(data.size() == 0)
            return null;

        int last = data.size() - 1;

        if(readFromRepository)
        {
            Log.d("Activities: ", "obtaining all data");

            resetTimes();
            for(SensorData activityList : data)
                obtainTimes((ActivityRecognitionDataList)activityList);
            readFromRepository = false;
        }
        else
        {
            obtainTimes((ActivityRecognitionDataList)data.get(last));
            Log.d("Activities: ", "obtaining last data");
        }
        return adaptOne();
    }

    @Override
    public boolean useLimit()
    {
        return true;
    }

    @Override
    public PieData adaptOne(SensorData data)
    {
        return null;
    }

    private void resetTimes()
    {
        activityTimes.put(DetectedActivity.WALKING, 0l);
        activityTimes.put(DetectedActivity.RUNNING, 0l);
        totalTime = 0;
    }


    public PieData adaptOne()
    {
        int runningTimeMin = (int)TimeUnit.MILLISECONDS.toMinutes(activityTimes.get(DetectedActivity.RUNNING));
        int walkingTimeMin = (int)TimeUnit.MILLISECONDS.toMinutes(activityTimes.get(DetectedActivity.WALKING));

        List<PieEntry> entries = new ArrayList<>();

        totalTime = runningTimeMin + walkingTimeMin;
        int restTime = activityGoal - totalTime;

        entries.add(new PieEntry(walkingTimeMin, "Walking"));
        entries.add(new PieEntry(runningTimeMin, "Running"));

        if(restTime > 0)
            entries.add(new PieEntry(restTime, "Remaining time"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setValueFormatter(new IValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler)
            {
                return String.valueOf((int)value);
            }
        });
        dataSet.setColors(Colors.ACTIVITY_COLORS);
        dataSet.setValueTextSize(10f);

        return new PieData(dataSet);
    }

    public int getTotalTime()
    {
        return totalTime;
    }

    @Override
    public ArrayList<PieData> adaptAll(ArrayList<SensorData> data)
    {
        return null;
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

    @Override
    public void setLimit(int limit)
    {
        activityGoal = limit;
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        if(data.size() == 0)
            return null;

        HashMap<String, ArrayList<SensorData>> dataByDay = partitionByDays(data);
        activityGoal *= dataByDay.size();

        resetTimes();
        for(SensorData activityList : data)
            obtainTimes((ActivityRecognitionDataList)activityList);
        readFromRepository = true;
        return adaptOne();
    }

    @Override
    public void prepareView(PieChart view)
    {
        view.setHoleRadius(80);
    }

    @Override
    public VisualizationAdapter<PieChart, PieData> newInstance()
    {
        return new ActivityPieAdapter();
    }

    @Override
    public boolean isAggregating()
    {
        return true;
    }

    private void obtainTimes(ActivityRecognitionDataList dataList)
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
                    activityTimes.put(activityType, activityTimes.get(activityType) + time);
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


}