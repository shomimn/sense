package com.mnm.sense.adapters;


import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.RunningApplicationData;
import com.ubhave.sensormanager.data.pull.RunningApplicationDataList;

import java.util.ArrayList;
import java.util.Map;

public class RunningApplicationBarAdapter extends VisualizationAdapter<BarChart,BarData>
{

    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        if(data.size() == 0)
            return null;

        int last = data.size() - 1;

        return adaptOne(data.get(last));
    }

    @Override
    public BarData adaptOne(SensorData data)
    {
        RunningApplicationDataList appDataList = (RunningApplicationDataList)data;

        BarData barData = new BarData();
        int i = 0;

        for (RunningApplicationData appData : appDataList.getRunningApplications())
        {
            ArrayList<BarEntry> barEntries = new ArrayList<>();

            BarEntry barEntry = new BarEntry(i++, appData.getForegroundTimeInMinutes());
            barEntries.add(barEntry);
            BarDataSet dataSet = new BarDataSet(barEntries, appData.getName());
            dataSet.setColor(ColorTemplate.COLORFUL_COLORS[i % ColorTemplate.COLORFUL_COLORS.length]);

            barData.addDataSet(dataSet);
        }


        barData.setBarWidth(0.9f);
        barData.setValueTextSize(10f);
        barData.setValueTextSize(10f);

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
        YAxis yAxis = view.getAxisLeft();

        yAxis.setDrawAxisLine(true);
        yAxis.setEnabled(true);

        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(2000f);

        yAxis.setValueFormatter(new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                return null;
            }
        });
    }

    @Override
    public VisualizationAdapter<BarChart, BarData> newInstance()
    {
        return new RunningApplicationBarAdapter();
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        return null;
    }
}
