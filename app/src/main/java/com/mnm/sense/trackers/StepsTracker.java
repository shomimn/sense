package com.mnm.sense.trackers;

import android.graphics.DashPathEffect;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.mnm.sense.models.BarChartModel;
import com.mnm.sense.models.TextModel;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.StepCounterData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class StepsTracker extends Tracker
{
    public StepsTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_STEP_COUNTER);

        text = "Steps";
        resource = R.drawable.ic_directions_walk;
        isOn = false;

        visualizations.put(Visualization.TEXT, new Visualization(1, 1, false));
        visualizations.put(Visualization.BAR_CHART, new Visualization(1, 3, false));

        adapters.put(Visualization.TEXT, new StepsTextAdapter());
        adapters.put(Visualization.BAR_CHART, new StepsDailyBarAdapter());
    }

    @Override
    public Object getModel(String visualizationType)
    {
        if (visualizationType.equals(Visualization.TEXT))
            return new TextModel(this, (String) super.getModel(visualizationType));
        else if (visualizationType.equals(Visualization.BAR_CHART))
            return new BarChartModel(this, (BarData) super.getModel(visualizationType));

        return null;
    }
}

class StepsTextAdapter implements VisualizationAdapter<TextView, String>
{
    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        int count = data.size();

        return adaptOne(data.get(count - 1));
    }

    @Override
    public String adaptOne(SensorData data)
    {
        StepCounterData stepsData = (StepCounterData) data;

        return String.valueOf(stepsData.getNumSteps());
    }

    @Override
    public ArrayList<String> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public void prepareView(TextView view)
    {

    }
}

class StepsDailyBarAdapter implements VisualizationAdapter<BarChart, BarData>
{
    float prevSteps = 0;
    HashMap<Integer, Float> counter = new HashMap<>();

    public StepsDailyBarAdapter()
    {
        for (int i = 1; i < 25; ++i)
            counter.put(i, 0f);
    }

    @Override
    public Object adapt(ArrayList<SensorData> data)
    {
        int count = data.size();

        return adaptOne(data.get(count - 1));
    }

    @Override
    public BarData adaptOne(SensorData data)
    {
        StepCounterData stepsData = (StepCounterData) data;
        float steps = stepsData.getNumSteps();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(stepsData.getTimestamp());
        int hour = cal.get(Calendar.HOUR);

        if (prevSteps != 0)
        {
            float diff = steps - prevSteps;
            Log.d("DIFF: ", String.valueOf(diff));
            updateCounter(hour, diff);
        }
        else
            updateCounter(hour, 0f);

        prevSteps = steps;

        BarData barData = new BarData();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (Map.Entry<Integer, Float> entry : counter.entrySet())
            entries.add(new BarEntry(entry.getKey(), entry.getValue()));

        BarDataSet dataSet = new BarDataSet(entries, "Steps");
        dataSet.setValueFormatter(new IValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler)
            {
                if (value > 0)
                    return String.valueOf(value);

                return "";
            }
        });

        barData.addDataSet(dataSet);
        barData.setBarWidth(0.1f);
        barData.setValueTextSize(6f);

        return barData;
    }

    @Override
    public ArrayList<BarData> adaptAll(ArrayList<SensorData> data)
    {
        return null;
    }

    @Override
    public void prepareView(BarChart view)
    {
        XAxis xAxis = view.getXAxis();

        xAxis.setDrawAxisLine(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                if (value % 4.0 == 0)
                    return String.valueOf((int)value);

                return ".";
            }
        });
    }

    void updateCounter(Integer key, Float increment)
    {
        if (counter.containsKey(key))
            counter.put(key, counter.get(key) + increment);
        else
            counter.put(key, increment);
    }
}
