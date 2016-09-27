package com.mnm.sense;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
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

        items = new ArrayList<>();
        items.add(new GridItem(1, trackerViewInitializer, new TrackerData("Steps", R.drawable.ic_directions_walk, true)));
        items.add(new GridItem(1, trackerViewInitializer, new TrackerData("Location", R.drawable.ic_my_location_black_48dp, true)));
        items.add(new GridItem(1, trackerViewInitializer, new TrackerData("WiFi", R.drawable.ic_wifi_black_48dp, false)));
        items.add(new GridItem(1, trackerViewInitializer, new TrackerData("Battery", R.drawable.ic_battery_full_black_48dp, false)));
        items.add(new GridItem(1, trackerViewInitializer, new TrackerData("Calls", R.drawable.ic_phone_in_talk_black_48dp, false)));
        items.add(new GridItem(1, trackerViewInitializer, new TrackerData("SMS", R.drawable.ic_sms_black_48dp, false)));
        items.add(new GridItem(1, trackerViewInitializer, new TrackerData("Pie Chart", R.drawable.ic_pie_chart_black_48dp, true)));
        items.add(new GridItem(1, trackerViewInitializer, new TrackerData("Visualize", R.drawable.ic_show_chart_black_48dp, true)));

        int row = 0;
        int col = 0;
        for (GridItem item : items)
        {
            CardView card = (CardView) inflater.inflate(R.layout.card_item, null);
            View view = null;

            view = (View) item.initializer.construct(getContext(), item.data);

            if (view == null)
                continue;

            card.addView(view);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.height = Util.dp(150);
            int margin = Util.dp(5);
            params.setMargins(margin, margin, margin, margin);
            GridLayout.Spec rowSpec = GridLayout.spec(row);
            GridLayout.Spec columnSpec = GridLayout.spec(col, item.columnSpan, 1);
            params.rowSpec = rowSpec;
            params.columnSpec = columnSpec;

            gridLayout.addView(card, params);

            col = (col + item.columnSpan) % gridLayout.getColumnCount();
            row = col == 0 ? row + 1 : row;
        }

        return scrollView;
    }
}
