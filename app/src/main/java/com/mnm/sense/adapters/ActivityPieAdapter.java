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
    private int activityGoal = 120;
    private boolean readFromRepository = false;

    ActivityMonitor monitor;

    public ActivityPieAdapter(ActivityMonitor monitor)
    {
        this.monitor = monitor;
    }

    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if(data.size() == 0)
            return null;

        int last = data.size() - 1;

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
        if(readFromRepository)
        {
            Log.d("Activities: ", "obtaining all data");

            monitor.resetTimes();
            for(SensorData activityList : data)
                monitor.obtainTimes((ActivityRecognitionDataList)activityList);
            readFromRepository = false;
        }
        else
        {
            monitor.obtainTimes((ActivityRecognitionDataList)data.get(last));
            Log.d("Activities: ", "obtaining last data");
        }

        int runningTimeMin = monitor.getTimeMin(DetectedActivity.RUNNING);
        int walkingTimeMin = monitor.getTimeMin(DetectedActivity.WALKING);

        List<PieEntry> entries = new ArrayList<>();

        int totalTime = monitor.getTotalTimes(DetectedActivity.RUNNING, DetectedActivity.WALKING);
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

    @Override
    public ArrayList<PieData> adaptAll(ArrayList<SensorData> data)
    {
        return null;
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

        monitor.resetTimes();
        for(SensorData activityList : data)
            monitor.obtainTimes((ActivityRecognitionDataList)activityList);

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
        return new ActivityPieAdapter(monitor);
    }

    @Override
    public boolean isAggregating()
    {
        return true;
    }
}
