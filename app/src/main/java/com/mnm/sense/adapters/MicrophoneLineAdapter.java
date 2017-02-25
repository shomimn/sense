package com.mnm.sense.adapters;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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

        ArrayList<Double> allDecibels = new ArrayList<>();

        for(SensorData data : dataList)
            allDecibels.addAll(((MicrophoneData)data).getDecibelsArray());

        List<Entry> entries = new ArrayList<>();

        int i = 0;
        for(Double decibel : allDecibels)
            entries.add(new Entry((float)i++, decibel.floatValue()));

        LineDataSet lineDataSet = new LineDataSet(entries, "Decibels");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData lineData = new LineData(dataSets);

        ArrayList<LineData> result = new ArrayList<>();
        result.add(lineData);

        return result;
    }

    @Override
    public VisualizationAdapter<LineChart, LineData> newInstance()
    {
        return null;
    }

    @Override
    public Object aggregate(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public void prepareView(LineChart view)
    {
        super.prepareView(view);
    }
}
