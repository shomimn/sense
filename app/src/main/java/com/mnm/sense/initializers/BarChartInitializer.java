package com.mnm.sense.initializers;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.mnm.sense.models.BarChartModel;
import com.mnm.sense.trackers.Tracker;
import com.ubhave.sensormanager.data.SensorData;

import java.util.ArrayList;

class LabelFormatter implements IAxisValueFormatter
{
    private final String[] mLabels;

    public LabelFormatter(String[] labels)
    {
        mLabels = labels;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis)
    {
        if (value < 0 || value > mLabels.length - 1)
            return "";

        return mLabels[(int) value];
    }
}

public class BarChartInitializer extends ViewInitializer<BarChart, BarChartModel>
{
    public BarChartInitializer()
    {
        super(BarChart.class, BarChartModel.class, Visualization.BAR_CHART);
    }

    @Override
    public void init(final Context context, final BarChart barChart, final BarChartModel model)
    {
        final AppCompatActivity activity = (AppCompatActivity) context;
        final VisualizationAdapter adapter = model.tracker.adapters.get(visualization);

        barChart.setData(model.data);
        barChart.fitScreen();
        barChart.setDescription(null);
        barChart.setDrawBorders(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setDrawLabels(false);

        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setAvoidFirstLastClipping(true);
//        xAxis.setCenterAxisLabels(true);

        final YAxis leftYAxis = barChart.getAxisLeft();
//        leftYAxis.setAxisMaximum(barChart.getData().getYMax());
        leftYAxis.setDrawGridLines(false);
        leftYAxis.setDrawLabels(false);
        leftYAxis.setDrawAxisLine(false);
        leftYAxis.setGranularity(1);
        leftYAxis.setGranularityEnabled(true);

        final YAxis rightYAxis = barChart.getAxisRight();
//        rightYAxis.setAxisMaximum(barChart.getData().getYMax());
        rightYAxis.setDrawGridLines(false);
        rightYAxis.setDrawLabels(false);
        rightYAxis.setDrawAxisLine(false);
        rightYAxis.setGranularity(1);
        rightYAxis.setGranularityEnabled(true);

        adapter.prepareView(barChart);

        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                barChart.animateY(500);
            }
        });

        if (model.shouldUpdate)
        {
            model.tracker.updateCallbacks.put(visualization, new Tracker.UpdateCallback()
            {
                @Override
                public void update(ArrayList<SensorData> with)
                {
                    final BarData barData = (BarData) adapter.adapt(with);

                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            barChart.setData(barData);

                            leftYAxis.setAxisMaximum(barData.getYMax());
                            rightYAxis.setAxisMaximum(barData.getYMax());

//                        barChart.animateY(500);
                            Log.d("SENSE", "Updating BarChart");
                            barChart.invalidate();
                        }
                    });
                }
            });
        }
    }
}
