package com.mnm.sense;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DashboardFragment extends Fragment
{
    GridLayout gridLayout;

    List<BarEntry> entries = new ArrayList<>(25);
    List<String> vals = new ArrayList<>(25);
    Random random = new Random();

    DynamicGrid grid;

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

        grid.addItem(new GridItem(2, 3, new DashboardData(DashboardData.MAP, R.drawable.ic_my_location_black_48dp, new LatLng(43.3, 21.9))));
        grid.addItem(new GridItem(1, 3, new DashboardData(DashboardData.BAR_CHART, R.drawable.ic_directions_walk, data)));
        grid.addItem(new GridItem(1, 1, new DashboardData(DashboardData.TEXT, R.drawable.ic_directions_walk, "5000/10000")));
        grid.addItem(new GridItem(1, 1, new DashboardData(DashboardData.TEXT, R.drawable.ic_phone_in_talk_black_48dp, "In: 6\nOut: 2\nMissed: 3")));
        grid.addItem(new GridItem(1, 1, new DashboardData(DashboardData.TEXT, R.drawable.ic_sms_black_48dp, "In: 15\nOut: 23")));
        grid.addItem(new GridItem(1, 1, "omg"));
        grid.addItem(new GridItem(1, 1, "it's"));
        grid.addItem(new GridItem(1, 1, "working"));

        grid.layoutItems(inflater);

        return scrollView;
    }
}
