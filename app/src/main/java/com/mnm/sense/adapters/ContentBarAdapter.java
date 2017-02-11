package com.mnm.sense.adapters;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class ContentBarAdapter extends ContentAdapter<BarChart, BarData>
{
    public ContentBarAdapter(String key)
    {
        super(key);
    }

    @Override
    protected BarData createFrom(HashMap<String, Integer> counter)
    {
        BarData barData = new BarData();
        int i = 0;

        for (Map.Entry<String, Integer> entry : counter.entrySet())
        {
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            BarEntry barEntry = new BarEntry(i++, entry.getValue().floatValue());
            barEntries.add(barEntry);
            BarDataSet dataSet = new BarDataSet(barEntries, entry.getKey());
//            dataSet.setColor(ColorTemplate.COLORFUL_COLORS[i]);

            barData.addDataSet(dataSet);
        }

        barData.setBarWidth(0.9f);
        barData.setValueTextSize(10f);
        barData.setValueTextSize(10f);

        return barData;
    }
}

