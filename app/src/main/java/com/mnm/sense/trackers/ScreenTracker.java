package com.mnm.sense.trackers;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mnm.sense.NotificationCreator;
import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.push.ScreenData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import org.joda.time.Hours;
import org.joda.time.Seconds;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ScreenTracker extends Tracker
{
    public static final String ATTRIBUTE_TIME = "Time";
    public static final long GUARD_SLEEP_INTERVAL = 60 * 60 * 1000;

    public boolean screenOn = true;
    public long prevTimestamp = 0;
    public long timeOn = 0;
    public long timeOff = 0;

    Runnable limitGuard = new Runnable()
    {
        @Override
        public void run()
        {
            Log.d("limitGuard", "Checking limits");

            long diff = System.currentTimeMillis() - prevTimestamp;

            if (screenOn)
            {
                long time = timeOn + diff;
                checkLimit(time);
            }

            handler.postDelayed(this, GUARD_SLEEP_INTERVAL);
        }
    };

    public ScreenTracker() throws ESException
    {
        super(SensorUtils.SENSOR_TYPE_SCREEN);

        text = "Screen";
        resource = R.drawable.ic_screen_lock_landscape_black_48dp;
        isOn = false;

        attributes = new String[] { ATTRIBUTE_TIME };

        visualizations.put(Visualization.TEXT, new Visualization(1, 1, false));
        visualizations.put(Visualization.PIE_CHART, new Visualization(1, 3, false));

        limit = new Limit("Daily limit", 4, 1, 12);

//        accent = android.R.color.holo_red_dark;
        accent = R.color.redColorAccent;
        theme = R.style.RedTheme;

        HashMap<String, VisualizationAdapter> timeAdapters = new HashMap<>();
        timeAdapters.put(Visualization.TEXT, new ScreenTextAdapter());
        timeAdapters.put(Visualization.PIE_CHART, new ScreenPieAdapter());

        adapters.put(ATTRIBUTE_TIME, timeAdapters);
    }

    @Override
    public void correctData(SensorData data)
    {
        ScreenData screenData = (ScreenData) data;
        long timestamp = screenData.getTimestamp();
        long diff = timestamp - prevTimestamp;
        screenOn = screenData.isOn();

        if (screenOn)
            timeOff += diff;
        else
            timeOn += diff;

        screenData.setTimeOff(timeOff);
        screenData.setTimeOn(timeOn);

        prevTimestamp = timestamp;
    }

    @Override
    public void limitNotification(SensorData data)
    {
        checkLimit(timeOn);
    }

    public void checkLimit(long time)
    {
        long timeOnSeconds = time / 1000;
        long limitSeconds = limit.value * 60 * 60;
//        long limitSeconds = 60;

//        Seconds timeOnSeconds = Seconds.seconds((int) timeOn / 1000);
//        Seconds limitSeconds = Hours.hours(limit.value).toStandardSeconds();

        if (timeOnSeconds >= limitSeconds)
//        if (timeOnSeconds.isGreaterThan(limitSeconds))
        {
            NotificationCreator.create(resource, "Sense", "Whoa, you've been using your phone too much! You should take a walk!");
        }
    }

    @Override
    public void start() throws ESException
    {
        prevTimestamp = System.currentTimeMillis();

        handler.postDelayed(limitGuard, GUARD_SLEEP_INTERVAL);

        super.start();
    }

    @Override
    public void pause() throws ESException
    {
        super.pause();

        handler.removeCallbacks(limitGuard);
    }

    @Override
    public void unpause() throws ESException
    {
        super.unpause();

        handler.postDelayed(limitGuard, GUARD_SLEEP_INTERVAL);
    }
}

class ScreenPieAdapter implements VisualizationAdapter<PieChart, PieData>
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
}

class ScreenTextAdapter implements VisualizationAdapter<TextView, String>
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
    public String adaptOne(SensorData data)
    {
        ScreenData screenData = (ScreenData) data;
        String result = "";

        result += "Time on: " + String.valueOf(screenData.getTimeOn() / 1000) + "\n"
               + "Time off: " + String.valueOf(screenData.getTimeOff() / 1000);

        return result;
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

