package com.mnm.sense;

import com.github.mikephil.charting.data.BarData;
import com.google.android.gms.maps.model.LatLng;
import com.mnm.sense.trackers.Tracker;

import java.util.HashMap;

public class Initializer
{
    private static HashMap<Class, ViewInitializer> byData = new HashMap<>();
    private static HashMap<Integer, ViewInitializer> byVisualization = new HashMap<>();
    private static Initializer instance = new Initializer();

    private Initializer()
    {
        byData.put(String.class, new TextViewInitializer());
        byData.put(Integer.class, new ImageViewInitializer());
        byData.put(BarData.class, new BarChartInitializer());
        byData.put(Tracker.class, new TrackerViewInitializer());
        byData.put(DashboardData.class, new DashboardViewInitializer());
        byData.put(LatLng.class, new MapFragmentInitializer());

        byVisualization.put(DashboardData.BAR_CHART, byData.get(BarData.class));
        byVisualization.put(DashboardData.TEXT, byData.get(String.class));
        byVisualization.put(DashboardData.MAP, byData.get(LatLng.class));
    }

    public static ViewInitializer get(Class key)
    {
        ViewInitializer initializer = instance.byData.get(key);

        if (initializer == null)
        {
            initializer = instance.byData.get(key.getSuperclass());
        }

        return initializer;
    }

    public static ViewInitializer get(Integer key)
    {
        return instance.byVisualization.get(key);
    }
}
