package com.mnm.sense.adapters;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.push.ScreenData;

import java.util.ArrayList;

public class ScreenPieAdapter extends VisualizationAdapter<PieChart, PieData>
{
    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        int size = data.size();

        if (size == 0)
            return null;

        return adaptOne(data.get(size - 1));
    }

    @Override
    public PieData adaptOne(SensorData data)
    {
        ScreenData screenData = (ScreenData) data;

        PieData pieData = new PieData();
        ArrayList<PieEntry> entries = new ArrayList<>();

        float timeOn = screenData.getTimeOn();
        float timeOff = screenData.getTimeOff();

        entries.add(new PieEntry(timeOn / 1000f / 60f, "Time on"));
        entries.add(new PieEntry(timeOff / 1000f / 60f, "Time off"));

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setSliceSpace(3f);
//        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        pieData.addDataSet(pieDataSet);
        pieData.setValueTextSize(10f);

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
        return new ScreenPieAdapter();
    }
}
