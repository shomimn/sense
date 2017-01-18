package com.mnm.sense.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.mnm.sense.DynamicGrid;
import com.mnm.sense.GridItem;
import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.models.DashboardModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DashboardFragment extends Fragment
{
    public GridLayout gridLayout;

    List<BarEntry> entries = new ArrayList<>(25);
    List<String> vals = new ArrayList<>(25);
    Random random = new Random();

    public DynamicGrid grid;

    public DashboardFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View scrollView = inflater.inflate(R.layout.grid_fragment, container, false);
        gridLayout = (GridLayout) scrollView.findViewById(R.id.gridLayout);

        grid = new DynamicGrid(gridLayout);

        for (int i = 0; i < 25; ++i)
        {
            entries.add(new BarEntry(i, random.nextFloat()));
            vals.add(String.valueOf(i));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Steps");
        dataSet.setColor(getResources().getColor(R.color.colorAccent));
        final BarData data = new BarData(dataSet);
        data.setBarWidth(0.2f);

//        grid.addItem(new GridItem(2, 3, new DashboardData(SenseApp.instance().tracker(SensorUtils.SENSOR_TYPE_LOCATION), 0, new LatLng(43.3, 21.9))));
//        grid.addItem(new GridItem(1, 3, new DashboardData(SenseApp.instance().tracker(SensorUtils.SENSOR_TYPE_STEP_COUNTER), 1, getModel)));
//        grid.addItem(new GridItem(1, 1, new DashboardData(SenseApp.instance().tracker(SensorUtils.SENSOR_TYPE_STEP_COUNTER), 0, "5000/10000")));
//        grid.addItem(new GridItem(1, 1, new DashboardData(SenseApp.instance().tracker(SensorUtils.SENSOR_TYPE_CALL_CONTENT_READER), 0, "In: 6\nOut: 2\nMissed: 3")));
//        grid.addItem(new GridItem(1, 1, new DashboardData(SenseApp.instance().tracker(SensorUtils.SENSOR_TYPE_SMS_CONTENT_READER), 0, "In: 15\nOut: 23")));
//        grid.addItem(new GridItem(1, 1, "omg"));
//        grid.addItem(new GridItem(1, 1, "it's"));
//        grid.addItem(new GridItem(1, 1, "working"));

//        grid.layoutItems(inflater);

        return scrollView;
    }

    public void addDashboardView(int rows, int cols, DashboardModel data)
    {
        boolean isMap = data.visualization.equals(Visualization.MAP);
        grid.addItem(new GridItem(rows, cols, data, isMap));
    }

    public void layoutDashboard()
    {
        grid.layoutItems((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }
}

