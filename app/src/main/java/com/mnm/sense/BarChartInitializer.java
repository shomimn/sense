package com.mnm.sense;


import android.content.Context;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;

public class BarChartInitializer extends ViewInitializer<BarChart, BarData>
{
    public BarChartInitializer()
    {
        super(BarChart.class);
    }

    @Override
    public void init(Context context, BarChart barChart, BarData data)
    {
        barChart.setData(data);
        barChart.fitScreen();
        barChart.setDescription(null);
        barChart.setDrawBorders(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis leftYAxis = barChart.getAxisLeft();
        leftYAxis.setDrawGridLines(false);
        leftYAxis.setDrawLabels(false);
        leftYAxis.setDrawAxisLine(false);

        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setDrawGridLines(false);
        rightYAxis.setDrawLabels(false);
        rightYAxis.setDrawAxisLine(false);
    }
}
