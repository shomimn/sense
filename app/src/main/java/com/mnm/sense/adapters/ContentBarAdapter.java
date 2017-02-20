package com.mnm.sense.adapters;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.mnm.sense.ColorGenerator;
import com.mnm.sense.Colors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class ContentBarAdapter extends ContentAdapter<BarChart, BarData>
{
    public ContentBarAdapter(String key)
    {
        super(key);
    }

    @Override
    protected BarData createFrom(HashMap<String, Integer> counter)
    {
        if (counter.size() == 0)
            return null;

        BarData barData = new BarData();
        int i = 0;
        int[] colors = ColorGenerator.generateRandom(counter.size());

        for (Map.Entry<String, Integer> entry : counter.entrySet())
        {
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            BarEntry barEntry = new BarEntry(i, entry.getValue().floatValue());
            barEntries.add(barEntry);
            BarDataSet dataSet = new BarDataSet(barEntries, entry.getKey());
//            dataSet.setColor(colors[i]);
            dataSet.setColor(Colors.CUSTOM_COLORS[i]);

            barData.addDataSet(dataSet);

            ++i;
        }

        barData.setBarWidth(0.9f);
        barData.setValueTextSize(10f);
        barData.setValueTextSize(10f);

        return barData;
    }
}

