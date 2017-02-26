package com.mnm.sense.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

