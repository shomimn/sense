package com.mnm.sense.adapters;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.mnm.sense.R;
import com.mnm.sense.SenseApp;
import com.mnm.sense.models.LineChartModel;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.MicrophoneData;

import java.util.ArrayList;
import java.util.List;

public class MicrophoneLineAdapter extends VisualizationAdapter<LineChart, LineData>
{

    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        int size = data.size();

        if (size == 0)
            return null;

        ArrayList<LineData> result = adaptAll(data);

        if(result.isEmpty())
            return null;

        return result.get(0);
    }

    @Override
    public LineData adaptOne(SensorData data)
    {
        return null;
    }

    @Override
    public ArrayList<LineData> adaptAll(ArrayList<SensorData> dataList)
    {

        ArrayList<Integer> allDecibels = new ArrayList<>();


        for(SensorData data : dataList)
            allDecibels.add(((MicrophoneData)data).getAverageDecibels());

        List<Entry> entries = new ArrayList<>();

        int i = 0;
        for(int decibel : allDecibels)
            entries.add(new Entry((float) i++, decibel));

        LineDataSet lineDataSet = new LineDataSet(entries, "Decibels fixed scale");

        lineDataSet.setDrawCircles(false);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        lineDataSet.setColor(SenseApp.context().getResources().getColor(R.color.redColorAccent));

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData lineData = new LineData(dataSets);

        ArrayList<LineData> result = new ArrayList<>();
        result.add(lineData);

        return result;
    }

    @Override
    public MicrophoneLineAdapter newInstance()
    {
        return new MicrophoneLineAdapter();
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public void prepareView(LineChart view)
    {

        YAxis yAxis = view.getAxisLeft();

        yAxis.setAxisMaximum(120f);
        yAxis.setAxisMinimum(0f);
        yAxis.setLabelCount(5, true);
        yAxis.setDrawAxisLine(true);
        yAxis.setEnabled(true);
        yAxis.setDrawLabels(true);
        yAxis.setDrawGridLines(true);
    }
}
