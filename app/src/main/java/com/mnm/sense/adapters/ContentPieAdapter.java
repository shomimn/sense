package com.mnm.sense.adapters;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mnm.sense.Colors;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.SMSContentListData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class ContentPieAdapter extends ContentAdapter<PieChart, PieData>
{
    public ContentPieAdapter(String key)
    {
        super(key);
    }

    @Override
    protected PieData createFrom(HashMap<String, Integer> counter)
    {
        PieData pieData = new PieData();
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : counter.entrySet())
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setColors(Colors.CUSTOM_COLORS);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setSliceSpace(3f);
//        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        pieData.addDataSet(pieDataSet);
        pieData.setValueTextSize(10f);

        return pieData;
    }
}

