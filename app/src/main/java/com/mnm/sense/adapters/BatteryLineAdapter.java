package com.mnm.sense.adapters;

import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mnm.sense.Colors;
import com.mnm.sense.R;
import com.mnm.sense.SenseApp;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.push.BatteryData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BatteryLineAdapter extends VisualizationAdapter<LineChart, LineData>
{
    public static final int UNIT = 10;

    public static final int X_MIN = 0;
    public static final int X_MAX = 24 * (60 / UNIT);

    private float yMin = 0;

    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        int size = data.size();

        if (size == 0)
            return null;

        return merge(adaptAll(data));
    }

    @Override
    public LineData adaptOne(SensorData data)
    {
        return null;
    }

    @Override
    public ArrayList<LineData> adaptAll(ArrayList<SensorData> data)
    {
        ArrayList<LineData> result = new ArrayList<>();
        TreeMap<Integer, Integer> counter = new TreeMap<>();
        Calendar cal = Calendar.getInstance();

//        for (SensorData sensorData : data)
        for (int i = 0; i < data.size(); ++i)
        {
            BatteryData batteryData = (BatteryData) data.get(i);

            cal.setTimeInMillis(batteryData.getTimestamp());

            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            int key = hour * (60 / UNIT) + (minute / UNIT);

            Log.d("BatteryLineAdapter", "i: " + String.valueOf(i) + ", ts: " + String.valueOf(batteryData.getTimestamp()) + ", key: " + String.valueOf(key));

            if (!counter.containsKey(key))
                counter.put(key, batteryData.getBatteryLevel());
        }

        for (Map.Entry<Integer, Integer> entry : counter.entrySet())
        {
            ArrayList<Entry> entries = new ArrayList<>();
            entries.add(new Entry(entry.getKey(), entry.getValue()));

            Log.d("BatterLineAdapter", "key: " + entry.getKey().toString());

            result.add(new LineData(new LineDataSet(entries, "")));
        }

        return result;
    }

    @Override
    public void prepareView(LineChart view)
    {
        view.setPinchZoom(false);
        view.setDoubleTapToZoomEnabled(false);

        XAxis xAxis = view.getXAxis();
        xAxis.setAxisMinimum(X_MIN);
        xAxis.setAxisMaximum(X_MAX);
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(25, true);

        xAxis.setValueFormatter(new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                int val = (int) value / (60 / UNIT);

                if (val % 4 == 0)
                    return String.valueOf(val);

                return ".";
            }
        });

        YAxis yAxis = view.getAxisLeft();
        yAxis.setAxisMinimum(Math.max(yMin - 9,0));
        yAxis.setGranularity(10);
        yAxis.setAxisMaximum(100);
        yAxis.setDrawGridLines(true);

        yAxis.setValueFormatter(new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                return String.valueOf((int)value) + "%";
            }
        });

        yAxis = view.getAxisRight();
        yAxis.setGranularity(10);
        yAxis.setAxisMinimum(Math.min(yMin - 9, 0));
        yAxis.setAxisMaximum(100);

        yMin = 0;
    }

    private LineData merge(ArrayList<LineData> data)
    {
        LineData result = new LineData();
        ArrayList<Entry> entries = new ArrayList<>();

        for (LineData lineData : data)
        {
            ILineDataSet ds = lineData.getDataSetByIndex(0);
            entries.add(ds.getEntryForIndex(0));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Percentage");
        dataSet.setDrawFilled(true);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircleHole(false);

        int color = Colors.CUSTOM_COLORS[0];
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        dataSet.setFillColor(color);

        yMin = dataSet.getYMin();

        if (dataSet.getEntryCount() > 1)
            dataSet.setDrawCircles(false);

        result.addDataSet(dataSet);

        return result;
    }

    @Override
    public VisualizationAdapter<LineChart, LineData> newInstance()
    {
        return new BatteryLineAdapter();
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

        int i = 0;
        float min = 100;
        LineData lineData = new LineData();
        ArrayList<LineDataSet> dataSets = new ArrayList<>(dataByDays.size());

        for (Map.Entry<String, ArrayList<SensorData>> entry : dataByDays.entrySet())
        {
            LineData dayLineData = (LineData) adapt(entry.getValue());
            LineDataSet dayDataSet = (LineDataSet) dayLineData.getDataSetByIndex(0);
            int color = Colors.CUSTOM_COLORS[i++ % Colors.CUSTOM_COLORS.length];

            dayDataSet.setLabel(entry.getKey());
            dayDataSet.setFillColor(color);
            min = Math.min(min, dayDataSet.getYMin());
            dayDataSet.setColor(color);

            lineData.addDataSet(dayDataSet);
        }

        yMin = min;

        return lineData;
    }
}
