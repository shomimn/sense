package com.mnm.sense;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DashboardFragment extends Fragment
{
    GridLayout gridLayout;

    List<BarEntry> entries = new ArrayList<>(25);
    List<String> vals = new ArrayList<>(25);
    Random random = new Random();

    ArrayList<GridItem> items;

    BarChartInitializer barChartInitializer = new BarChartInitializer();
    ImageViewInitializer imageViewInitializer = new ImageViewInitializer();
    TrackerViewInitializer trackerViewInitializer = new TrackerViewInitializer();

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

        for (int i = 0; i < 25; ++i)
        {
            entries.add(new BarEntry(i, random.nextFloat()));
            vals.add(String.valueOf(i));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Steps");
        dataSet.setColor(getResources().getColor(R.color.colorAccent));
        final BarData data = new BarData(dataSet);
        data.setBarWidth(0.2f);

        items = new ArrayList<>();
        items.add(new GridItem(3, barChartInitializer, data));
        items.add(new GridItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new GridItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new GridItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new GridItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new GridItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new GridItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new GridItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new GridItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new GridItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new GridItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new GridItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new GridItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new GridItem(3, barChartInitializer, data));
        items.add(new GridItem(2, barChartInitializer, data));
        items.add(new GridItem(1, imageViewInitializer, R.mipmap.ic_launcher));


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
