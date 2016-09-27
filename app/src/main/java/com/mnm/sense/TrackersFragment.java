package com.mnm.sense;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrackersFragment extends Fragment
{
    GridLayout gridLayout;

    List<BarEntry> entries = new ArrayList<>(25);
    List<String> vals = new ArrayList<>(25);
    Random random = new Random();

    ArrayList<GridItem> items;

    BarChartInitializer barChartInitializer = new BarChartInitializer();
    ImageViewInitializer imageViewInitializer = new ImageViewInitializer();
    TrackerViewInitializer trackerViewInitializer = new TrackerViewInitializer();

    DynamicGrid grid;

    public TrackersFragment()
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
        View scrollView = inflater.inflate(R.layout.grid_fragment, container, false);
        gridLayout = (GridLayout) scrollView.findViewById(R.id.gridLayout);

        grid = new DynamicGrid(gridLayout);

        grid.addItem(new GridItem(1, 1, trackerViewInitializer, new TrackerData("Steps", R.drawable.ic_directions_walk, true)));
        grid.addItem(new GridItem(1, 1, trackerViewInitializer, new TrackerData("Location", R.drawable.ic_my_location_black_48dp, true)));
        grid.addItem(new GridItem(1, 1, trackerViewInitializer, new TrackerData("WiFi", R.drawable.ic_wifi_black_48dp, false)));
        grid.addItem(new GridItem(1, 1, trackerViewInitializer, new TrackerData("Battery", R.drawable.ic_battery_full_black_48dp, false)));
        grid.addItem(new GridItem(1, 1, trackerViewInitializer, new TrackerData("Calls", R.drawable.ic_phone_in_talk_black_48dp, false)));
        grid.addItem(new GridItem(1, 1, trackerViewInitializer, new TrackerData("SMS", R.drawable.ic_sms_black_48dp, false)));
        grid.addItem(new GridItem(1, 1, trackerViewInitializer, new TrackerData("Pie Chart", R.drawable.ic_pie_chart_black_48dp, true)));
        grid.addItem(new GridItem(1, 1, trackerViewInitializer, new TrackerData("Visualize", R.drawable.ic_show_chart_black_48dp, true)));

        grid.layoutItems(inflater);

        return scrollView;
    }
}
