package com.mnm.sense.initializers;

import com.mnm.sense.Visualization;
import com.mnm.sense.models.BarChartModel;
import com.mnm.sense.models.DashboardModel;
import com.mnm.sense.models.ListViewModel;
import com.mnm.sense.models.MapModel;
import com.mnm.sense.models.PieChartModel;
import com.mnm.sense.models.TextModel;
import com.mnm.sense.models.UpdateViewModel;
import com.mnm.sense.trackers.Tracker;

import java.util.HashMap;

public class Initializer
{
    private static HashMap<Class, ViewInitializer> byData = new HashMap<>();
    private static HashMap<String, ViewInitializer> byVisualization = new HashMap<>();
    private static Initializer instance = new Initializer();

    private Initializer()
    {
        byData.put(TextModel.class, new TextViewInitializer());
        byData.put(Integer.class, new ImageViewInitializer());
        byData.put(BarChartModel.class, new BarChartInitializer());
        byData.put(Tracker.class, new TrackerViewInitializer());
        byData.put(DashboardModel.class, new DashboardViewInitializer());
        byData.put(MapModel.class, new MapFragmentInitializer());
        byData.put(UpdateViewModel.class, new UpdateViewInitializer());
        byData.put(PieChartModel.class, new PieChartInitializer());
        byData.put(ListViewModel.class, new ListViewInitializer());

        byVisualization.put(Visualization.BAR_CHART, byData.get(BarChartModel.class));
        byVisualization.put(Visualization.TEXT, byData.get(TextModel.class));
        byVisualization.put(Visualization.MAP, byData.get(MapModel.class));
        byVisualization.put(Visualization.PIE_CHART, byData.get(PieChartModel.class));
        byVisualization.put(Visualization.LIST_VIEW, byData.get(ListViewModel.class));
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

    public static ViewInitializer get(String key)
    {
        return instance.byVisualization.get(key);
    }
}
