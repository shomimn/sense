package com.mnm.sense.trackers;


import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.push.BatteryData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BatteryTracker extends Tracker
{
    public static final String ATTRIBUTE_PERCENT = "Percent";

    public BatteryTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_BATTERY);

        text = "Battery";
        resource = R.drawable.ic_battery_full_black_48dp;
        isOn = false;

        attributes = new String[]{ ATTRIBUTE_PERCENT };

        visualizations.put(Visualization.LINE_CHART, new Visualization(1, 3, false));

        HashMap<String, VisualizationAdapter> percentAdapters = new HashMap<>();
        percentAdapters.put(Visualization.LINE_CHART, new BatteryLineAdapter());

        adapters.put(ATTRIBUTE_PERCENT, percentAdapters);
    }
}

class BatteryLineAdapter implements VisualizationAdapter<LineChart, LineData>
{
    public static final int UNIT = 10;

    public static final int X_MIN = 0;
    public static final int X_MAX = 24 * (60 / UNIT);

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
        XAxis xAxis = view.getXAxis();
        xAxis.setAxisMinimum(X_MIN);
        xAxis.setAxisMaximum(X_MAX);

        YAxis yAxis = view.getAxisLeft();
        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(100);
        yAxis.setDrawGridLines(true);

        yAxis = view.getAxisRight();
        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(100);
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

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setDrawFilled(true);
        dataSet.setDrawValues(false);
//        dataSet.setDrawCircles(false);
        dataSet.setDrawCircleHole(false);
//        dataSet.setCircleColor(ColorTemplate.MATERIAL_COLORS[0]);
//        dataSet.setColor(ColorTemplate.MATERIAL_COLORS[0]);
//        dataSet.setFillColor(ColorTemplate.MATERIAL_COLORS[0]);

        if (dataSet.getEntryCount() > 1)
            dataSet.setDrawCircles(false);

        result.addDataSet(dataSet);

        return result;
    }
}

