package com.mnm.sense.fragments;


import android.content.Intent;
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
import com.mnm.sense.Util;
import com.mnm.sense.Visualization;
import com.mnm.sense.activities.MainActivity;
import com.mnm.sense.activities.SecondActivity;
import com.mnm.sense.models.ButtonModel;
import com.mnm.sense.models.UpdateViewModel;
import com.mnm.sense.trackers.MergedTracker;
import com.mnm.sense.trackers.Tracker;
import com.ubhave.sensormanager.ESException;

import java.util.ArrayList;

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
        grid.rowHeight = Util.dp(50);

//        grid.setMargins(Util.dp(5), Util.dp(3), Util.dp(5), Util.dp(3));

        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ArrayList<Tracker> selected = new ArrayList<>();
//                ArrayList<AttributedFeature> features = new ArrayList<>();

                for (Tracker tracker : SenseApp.instance().trackers.values())
                    if (tracker.selected)
                        selected.add(tracker);

                try
                {
                    MergedTracker mergedTracker = new MergedTracker(Visualization.MAP, selected);
                    mergedTracker.text = "God";
                    mergedTracker.type = 123;
                    mergedTracker.attributes = new String[]{ "Everything." };
                    mergedTracker.accent = R.color.redColorAccent;
                    mergedTracker.theme = R.style.RedTheme;

                    SenseApp.instance().addMergedTracker(123, mergedTracker);

                    Intent intent = new Intent(getContext(), SecondActivity.class);
                    intent.putExtra("tracker", 123);
                    intent.putExtra("visualization", Visualization.MAP);
                    intent.putExtra("merge", true);

                    startActivity(intent);
                }
                catch (ESException e)
                {
                    e.printStackTrace();
                }
            }
        };

        grid.addItem(new GridItem(1, 3, new ButtonModel("VIEW SELECTION ON MAP", listener)));

        grid.addItem(new GridItem(2, 3, new UpdateViewModel()));

        for (Tracker tracker : SenseApp.instance().trackers.values())
            grid.addItem(new GridItem(3, 1, tracker));


        grid.layoutItems(inflater);

        return scrollView;
    }
}
