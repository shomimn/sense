package com.mnm.sense.adapters;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mnm.sense.Colors;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.RunningApplicationData;
import com.ubhave.sensormanager.data.pull.RunningApplicationDataList;

import java.util.ArrayList;


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

        int i = 0;
        BarData barData = new BarData();

        for (RunningApplicationData appData : appDataList.getMostUsedApplications())
        {
            ArrayList<BarEntry> barEntries = new ArrayList<>();

            barEntries.add(new BarEntry(i++, appData.getForegroundTimeMins()));
            BarDataSet dataSet = new BarDataSet(barEntries, appData.getName());
            dataSet.setColor(Colors.CUSTOM_COLORS[i % appDataList.getMostUsedApplications().size()]);
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
//        yAxis.setAxisMinimum(0f);
//        yAxis.setAxisMaximum(2000f);
        yAxis.setDrawLabels(true);
//        yAxis.setValueFormatter(new IAxisValueFormatter()
//        {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis)
//            {
//                return String.valueOf(value) + "";
//            }
//        });
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
