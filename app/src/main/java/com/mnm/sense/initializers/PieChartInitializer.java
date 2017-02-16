package com.mnm.sense.initializers;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.mnm.sense.models.PieChartModel;
import com.mnm.sense.trackers.Tracker;
import com.ubhave.sensormanager.data.SensorData;

import java.util.ArrayList;

public class PieChartInitializer extends ViewInitializer<PieChart, PieChartModel>
{
    public PieChartInitializer()
    {
        super(PieChart.class, PieChartModel.class, Visualization.PIE_CHART);
    }

    @Override
    public void init(Context context, final PieChart view, final PieChartModel model)
    {
        final AppCompatActivity activity = (AppCompatActivity) context;
        final VisualizationAdapter adapter = model.tracker.defaultAdapter(visualization);

        view.setData(model.data);
        view.setDrawEntryLabels(false);

        Description desc = new Description();
        desc.setText("");
        view.setDescription(desc);
//        view.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
//        view.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        view.getLegend().setWordWrapEnabled(true);
//        view.getLegend().setEnabled(false);
        view.setRotationEnabled(false);

        adapter.prepareView(view);

        view.animateY(500);

        if (model.shouldUpdate)
        {
            model.tracker.updateCallbacks.put(visualization, new Tracker.UpdateCallback()
            {
                @Override
                public void update(ArrayList<SensorData> with)
                {
                    final PieData pieData = (PieData) model.tracker.defaultAdapter(visualization).adapt(with);

                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            view.setData(pieData);

                            Log.d("SENSE", "Updating PieChart");
                            view.invalidate();
                        }
                    });
                }
            });
        }
    }
}
