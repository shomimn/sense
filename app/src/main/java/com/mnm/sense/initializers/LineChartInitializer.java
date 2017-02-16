package com.mnm.sense.initializers;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.mnm.sense.models.LineChartModel;
import com.mnm.sense.trackers.Tracker;
import com.ubhave.sensormanager.data.SensorData;

import java.util.ArrayList;

public class LineChartInitializer extends ViewInitializer<LineChart, LineChartModel>
{
    public LineChartInitializer()
    {
        super(LineChart.class, LineChartModel.class, Visualization.LINE_CHART);
    }

    @Override
    public void init(final Context context, final LineChart view, final LineChartModel model)
    {
        final AppCompatActivity activity = (AppCompatActivity) context;
        final Tracker tracker = model.tracker;
        final VisualizationAdapter adapter = tracker.defaultAdapter(visualization);

        view.setData(model.data);

        Description desc = new Description();
        desc.setText("");
        view.setDescription(desc);

        XAxis xAxis = view.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        YAxis leftYAxis = view.getAxisLeft();
        leftYAxis.setGranularityEnabled(true);
        leftYAxis.setGranularity(1);
        leftYAxis.setDrawGridLines(false);

        YAxis rightYAxis = view.getAxisRight();
        rightYAxis.setGranularityEnabled(true);
        rightYAxis.setGranularity(1);
        rightYAxis.setDrawGridLines(false);
        rightYAxis.setDrawLabels(false);

        adapter.prepareView(view);

        view.animateY(500);

        if (model.shouldUpdate)
        {
            tracker.updateCallbacks.put(visualization, new Tracker.UpdateCallback()
            {
                @Override
                public void update(final ArrayList<SensorData> with)
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            LineData lineData = (LineData) adapter.adapt(with);

                            view.setData(lineData);
                            adapter.prepareView(view);
                            view.invalidate();
                        }
                    });
                }
            });
        }
    }
}
