package com.mnm.sense.adapters;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.mnm.sense.Colors;
import com.mnm.sense.R;
import com.mnm.sense.SenseApp;
import com.ubhave.sensormanager.data.SensorData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CameraBarAdapter extends VisualizationAdapter<BarChart, BarData>
{
    @Override
    public BarData adapt(ArrayList<SensorData> data)
    {
        if (data.size() == 0)
            return null;

        return adaptAll(data).get(0);
    }

    @Override
    public BarData adaptOne(SensorData data)
    {
        return null;
    }

    @Override
    public ArrayList<BarData> adaptAll(ArrayList<SensorData> data)
    {
        ArrayList<BarData> result = new ArrayList<>();
        BarData barData = new BarData();

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, data.size()));

        BarDataSet dataSet = new BarDataSet(entries, "Photos taken today");
        barData.addDataSet(dataSet);
        dataSet.setColor(SenseApp.context().getResources().getColor(R.color.colorAccent));

        result.add(barData);

        return result;
    }

    @Override
    public CameraBarAdapter newInstance()
    {
        return new CameraBarAdapter();
    }

    @Override
    public boolean isAggregating()
    {
        return true;
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        HashMap<String, ArrayList<SensorData>> dataByDays = partitionByDays(data);
        BarData result = new BarData();

        int i = 0;
        for (Map.Entry<String, ArrayList<SensorData>> dailyData : dataByDays.entrySet())
        {
            BarData barData = adapt(dailyData.getValue());
            BarDataSet dataSet = (BarDataSet) barData.getDataSetByIndex(0);
            BarEntry entry = dataSet.getEntryForIndex(0);

            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(i, entry.getY()));

            BarDataSet newDataSet = new BarDataSet(entries, dailyData.getKey());
            newDataSet.setColor(Colors.CUSTOM_COLORS[i % Colors.CUSTOM_COLORS.length]);

            result.addDataSet(newDataSet);

            ++i;
        }

        return result;
    }
}
