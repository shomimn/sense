package com.mnm.sense.adapters;


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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityPieAdapter extends VisualizationAdapter<PieChart, PieData>
{
    private int activityGoal = 120;
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

        return adaptOne(data.get(last));
    }

    @Override
    public boolean useLimit()
    {
        return true;
    }

    @Override
    public PieData adaptOne(SensorData data)
    {
        return getPieData(monitor.getLiveTracker());
    }

    private PieData getPieData(ActivityMonitor.ActivityTimeTracker tracker)
    {
        List<PieEntry> entries = new ArrayList<>();

        int totalTime = tracker.getMinutes(DetectedActivity.WALKING, DetectedActivity.RUNNING);
        int restTime = activityGoal - totalTime;

        entries.add(new PieEntry(tracker.getMinutes(DetectedActivity.WALKING), "Walking"));
        entries.add(new PieEntry(tracker.getMinutes(DetectedActivity.RUNNING), "Running"));

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

        ActivityMonitor.ActivityTimeTracker timeTracker = monitor.monitorPortion(data);

        return getPieData(timeTracker);
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
