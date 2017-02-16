package com.mnm.sense.adapters;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.mnm.sense.Colors;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.RunningApplicationData;
import com.ubhave.sensormanager.data.pull.RunningApplicationDataList;

import java.util.ArrayList;
import java.util.List;

public class RunningApplicationPieAdapter extends VisualizationAdapter<PieChart, PieData>
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
    public PieData adaptOne(SensorData data)
    {
        RunningApplicationDataList appDataList = (RunningApplicationDataList) data;

        List<PieEntry> entries = new ArrayList<>();

        long totalForegroundTime = appDataList.getTotalForegroundTime();

        for(RunningApplicationData appData : appDataList.getMostUsedApplications())
        {
            float percentage = appData.getForegroundTime() * 100 / totalForegroundTime;
            entries.add(new PieEntry(percentage, appData.getName()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(Colors.CUSTOM_COLORS);
        dataSet.setValueTextSize(10f);
        dataSet.setSliceSpace(3f);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        return pieData;
    }

    @Override
    public ArrayList<PieData> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public void prepareView(PieChart view)
    {

    }

    @Override
    public VisualizationAdapter<PieChart, PieData> newInstance()
    {
        return new RunningApplicationPieAdapter();
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        return null;
    }
}
