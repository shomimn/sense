package com.mnm.sense.adapters;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.mnm.sense.R;
import com.mnm.sense.SenseApp;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.StepCounterData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class StepsHourlyBarAdapter extends VisualizationAdapter<BarChart, BarData>
{
    float[] counter = new float[24];

    public StepsHourlyBarAdapter()
    {
        for (int i = 0; i < 24; ++i)
            counter[i] = 0f;
    }

    private BarData createFrom(float[] counter)
    {
        BarData barData = new BarData();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < 24; ++i)
            entries.add(new BarEntry(i, counter[i]));

        BarDataSet dataSet = new BarDataSet(entries, "Steps");
        dataSet.setColor(SenseApp.context().getResources().getColor(R.color.colorAccent));
        dataSet.setValueFormatter(new IValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler)
            {
                if (value > 0)
                    return String.valueOf(value);

                return "";
            }
        });

        barData.addDataSet(dataSet);
        barData.setBarWidth(0.1f);
        barData.setValueTextSize(6f);

        return barData;
    }

    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        int count = data.size();

        return adaptOne(data.get(count - 1));
    }

    @Override
    public BarData adaptOne(SensorData data)
    {
        StepCounterData stepsData = (StepCounterData) data;
        float steps = stepsData.getNumSteps();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(stepsData.getTimestamp());
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if (counter[hour] < steps)
            counter[hour] = steps;

        return createFrom(counter);
    }

    @Override
    public ArrayList<BarData> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public void prepareView(BarChart view)
    {
        XAxis xAxis = view.getXAxis();

        xAxis.setDrawAxisLine(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setLabelCount(24, true);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(23f);

        xAxis.setValueFormatter(null);

        xAxis.setValueFormatter(new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                int val = (int) value;
                if (val % 4 == 0)
                    return String.valueOf(val);

                return ".";
            }
        });
    }

    @Override
    public VisualizationAdapter<BarChart, BarData> newInstance()
    {
        return new StepsHourlyBarAdapter();
    }

    @Override
    public boolean isAggregating()
    {
        return true;
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        HashMap<String, ArrayList<SensorData>> dataByDay = partitionByDays(data);
        ArrayList<float[]> counters = new ArrayList<>(dataByDay.size());
        Calendar cal = Calendar.getInstance();

        for (ArrayList<SensorData> dailyData : dataByDay.values())
        {
            float[] counter = new float[24];

            for (SensorData sensorData : dailyData)
            {
                StepCounterData stepsData = (StepCounterData) sensorData;
                cal.setTimeInMillis(stepsData.getTimestamp());

                int hour = cal.get(Calendar.HOUR_OF_DAY);
                float steps = stepsData.getNumSteps();

                if (counter[hour] < steps)
                    counter[hour] = steps;
            }

            counters.add(counter);
        }

        float[] totalCounter = new float[24];

        for (float[] counter : counters)
            for (int i = 0; i < 24; ++i)
                totalCounter[i] += counter[i];

        return createFrom(totalCounter);
    }
}

