package com.mnm.sense.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mnm.sense.DynamicGrid;
import com.mnm.sense.GridItem;
import com.mnm.sense.R;
import com.mnm.sense.SenseApp;
import com.mnm.sense.models.UpdateViewModel;
import com.mnm.sense.trackers.Tracker;

import java.util.Map;

public class TrackersFragment extends Fragment
{
    GridLayout gridLayout;
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

        grid.addItem(new GridItem(1, 3, new UpdateViewModel()));

        for (Tracker tracker : SenseApp.instance().trackers.values())
            grid.addItem(new GridItem(1, 1, tracker));

         grid.layoutItems(inflater);

        return scrollView;
    }
}
