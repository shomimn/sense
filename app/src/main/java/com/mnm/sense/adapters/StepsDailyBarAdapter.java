package com.mnm.sense.adapters;

import android.util.Log;
import android.util.SparseArray;

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
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.StepCounterData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class StepsDailyBarAdapter extends VisualizationAdapter<BarChart, BarData>
{
    float[] counter = new float[8];
    SparseArray<String> weekDays = new SparseArray<>();

    public StepsDailyBarAdapter()
    {
        weekDays.append(1, "Sun");
        weekDays.append(2, "Mon");
        weekDays.append(3, "Tue");
        weekDays.append(4, "Wed");
        weekDays.append(5, "Thu");
        weekDays.append(6, "Fri");
        weekDays.append(7, "Sat");
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
        int day = cal.get(Calendar.DAY_OF_WEEK);

        counter[day] = steps;

        BarData barData = new BarData();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 1; i < 8; ++i)
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
        barData.setBarWidth(0.5f);
        barData.setValueTextSize(6f);

        return barData;
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
        xAxis.resetAxisMaximum();
        xAxis.resetAxisMinimum();
//        xAxis.setAxisMaximum(7f);
        xAxis.setLabelCount(7, true);
//        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(1f);
//        xAxis.setAxisMaximum(7f);

        xAxis.setValueFormatter(null);

        xAxis.setValueFormatter(new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                Log.d("formatter", String.valueOf(value));
                int val = (int) value;

                if (val < 8)
                    return weekDays.valueAt(val);

                return ".";
            }
        });
    }

    @Override
    public VisualizationAdapter<BarChart, BarData> newInstance()
    {
        return new StepsDailyBarAdapter();
    }

    @Override
    public boolean isAggregating()
    {
        return true;
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        HashMap<String, ArrayList<SensorData>> dataByDay = partitionByDays(data);

        return null;
    }
}
