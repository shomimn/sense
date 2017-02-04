package com.mnm.sense.trackers;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.mnm.sense.NotificationCreator;
import com.mnm.sense.R;
import com.mnm.sense.SenseApp;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.mnm.sense.models.BarChartModel;
import com.mnm.sense.models.TextModel;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.StepCounterData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StepsTracker extends Tracker
{
    float firstCount = 0;
    float steps = 0;

    public StepsTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_STEP_COUNTER);

        text = "Steps";
        resource = R.drawable.ic_directions_walk;
        isOn = false;

        limit = new Limit("Daily goal", 1000, 20000);

        visualizations.put(Visualization.TEXT, new Visualization(1, 1, false));
        visualizations.put(Visualization.BAR_CHART, new Visualization(1, 3, false));

        adapters.put(Visualization.TEXT, new StepsTextAdapter());
        adapters.put(Visualization.BAR_CHART, new StepsDailyBarAdapter());

        getConfig().edit().clear().commit();
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

    @Override
    public void limitNotification(SensorData data)
    {
        SharedPreferences prefs = getConfig();

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        DateFormat dateFormat = DateFormat.getDateInstance();
        String stringDate = dateFormat.format(date);

        if (prefs.getBoolean(stringDate, false))
            return;

        StepCounterData stepsData = (StepCounterData) data;
        if (stepsData.getNumSteps() >= limit.value)
        {
            NotificationCreator.create(resource, "Sense", "Well done, you've reached your daily steps goal!");

            prefs.edit().putBoolean(stringDate, true).commit();
        }
    }

    @Override
    public void correctData(SensorData data)
    {
        StepCounterData stepsData = (StepCounterData) data;
        float lastCount = stepsData.getNumSteps();

        if (firstCount == 0)
        {
            firstCount = lastCount;
            stepsData.setNumSteps(0);

            return;
        }

        steps = lastCount - firstCount;
        stepsData.setNumSteps(steps);
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
    float[] counter = new float[24];

    public StepsDailyBarAdapter()
    {
        for (int i = 0; i < 24; ++i)
            counter[i] = 0f;
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
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        counter[hour] = steps;

        BarData barData = new BarData();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < 24; ++i)
            entries.add(new BarEntry(i, counter[i]));

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
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                if (value % 4.0 == 0 || value == 23)
                    return String.valueOf((int)value);

                return "";
            }
        });
    }
}
