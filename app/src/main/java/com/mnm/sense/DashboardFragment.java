package com.mnm.sense;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.estimote.sdk.EstimoteSDK.getApplicationContext;

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
    TextViewInitializer textViewInitializer = new TextViewInitializer();
    DashboardViewInitializer dashboardViewInitializer = new DashboardViewInitializer();
    MapViewInitializer mapViewInitializer = new MapViewInitializer();
    LinearLayoutInitializer linearLayoutInitializer = new LinearLayoutInitializer();

    DynamicGrid grid;

    public class TextViewInitializer extends ViewInitializer<TextView, String>
    {
        public TextViewInitializer()
        {
            super(TextView.class);
        }

        @Override
        public void init(TextView view, String data)
        {
            view.setText(data);
            view.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    public class MapViewInitializer extends ViewInitializer<MapView, LatLng>
    {
        public MapViewInitializer()
        {
            super(MapView.class);
        }

        @Override
        public void init(MapView view, final LatLng data)
        {
            view.getMapAsync(new OnMapReadyCallback()
            {
                @Override
                public void onMapReady(GoogleMap googleMap)
                {
                    map = googleMap;
                    googleMap.addMarker(new MarkerOptions().position(data));
                }
            });
        }
    }

    public class LinearLayoutInitializer extends ViewInitializer<LinearLayout, LatLng>
    {
        LinearLayout parent;
        public LinearLayoutInitializer()
        {
            super(LinearLayout.class);
        }

        public void setParent(LinearLayout layout)
        {
            parent = layout;
        }

        @Override
        public void init(LinearLayout view, LatLng data)
        {
//            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            inflater.inflate(R.layout.dashboard_card, view, true);
            AppCompatActivity activity = (AppCompatActivity) getContext();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            SupportMapFragment mapFragment = new SupportMapFragment();

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(parent.getId(), mapFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public class DashboardViewInitializer extends ViewInitializer<DashboardView, DashboardData>
    {
        public DashboardViewInitializer()
        {
            super(DashboardView.class);
        }

        @Override
        public void init(DashboardView view, DashboardData data)
        {
            View subview = null;
            LinearLayout parent = (LinearLayout) view.findViewById(R.id.layout);

            if (data.type == DashboardData.BAR_CHART)
            {
                subview = barChartInitializer.construct(SenseApp.context(), (BarData) data.data);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            params.weight = 3.5f;
                params.gravity = Gravity.CENTER;

//            parent.addView(subview, 1, params);
                parent.addView(subview, params);
            }
            else if (data.type == DashboardData.COUNT)
            {
                subview = textViewInitializer.construct(SenseApp.context(), (String) data.data);
                TextView textView = (TextView) subview;
                textView.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            params.weight = 3.5f;
                params.gravity = Gravity.CENTER;

//            parent.addView(subview, 1, params);
                parent.addView(subview, params);
            }
            else if (data.type == DashboardData.PIE_CHART)
            {
                linearLayoutInitializer.setParent(parent);
                subview = linearLayoutInitializer.construct(SenseApp.context(), (LatLng) data.data);
            }


            view.image.setImageResource(data.imageResource);
        }
    }

    GoogleMap map;

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

//        grid.addItem(new GridItem(2, 3, barChartInitializer, data));
//        grid.addItem(new GridItem(1, 1, imageViewInitializer, R.mipmap.ic_launcher));
//        grid.addItem(new GridItem(1, 1, imageViewInitializer, R.mipmap.ic_launcher));
//        grid.addItem(new GridItem(1, 1, imageViewInitializer, R.mipmap.ic_launcher));
//        grid.addItem(new GridItem(1, 1, imageViewInitializer, R.mipmap.ic_launcher));
//        grid.addItem(new GridItem(1, 1, imageViewInitializer, R.mipmap.ic_launcher));
//        grid.addItem(new GridItem(1, 1, imageViewInitializer, R.mipmap.ic_launcher));
//        grid.addItem(new GridItem(1, 1, imageViewInitializer, R.mipmap.ic_launcher));
//        grid.addItem(new GridItem(1, 1, imageViewInitializer, R.mipmap.ic_launcher));
//        grid.addItem(new GridItem(1, 1, imageViewInitializer, R.mipmap.ic_launcher));
//        grid.addItem(new GridItem(1, 1, imageViewInitializer, R.mipmap.ic_launcher));
//        grid.addItem(new GridItem(1, 1, imageViewInitializer, R.mipmap.ic_launcher));
//        grid.addItem(new GridItem(1, 1, imageViewInitializer, R.mipmap.ic_launcher));
//        grid.addItem(new GridItem(1, 3, barChartInitializer, data));
//        grid.addItem(new GridItem(1, 2, barChartInitializer, data));
//        grid.addItem(new GridItem(1, 1, imageViewInitializer, R.mipmap.ic_launcher));

//        grid.addItem(new GridItem(1, 3, dashboardViewInitializer, new DashboardData(DashboardData.BAR_CHART, R.drawable.ic_directions_walk, data)));
//        grid.addItem(new GridItem(1, 3, dashboardViewInitializer, new DashboardData(DashboardData.COUNT, R.drawable.ic_directions_walk, "5000/10000")));
//        grid.addItem(new GridItem(1, 1, dashboardViewInitializer, new DashboardData(DashboardData.COUNT, R.drawable.ic_phone_in_talk_black_48dp, "In: 6\nOut: 2\nMissed: 3")));
//        grid.addItem(new GridItem(1, 1, dashboardViewInitializer, new DashboardData(DashboardData.COUNT, R.drawable.ic_sms_black_48dp, "In: 15\nOut: 23")));
        grid.addItem(new GridItem(2, 3, dashboardViewInitializer, new DashboardData(DashboardData.PIE_CHART, R.drawable.ic_my_location_black_48dp, new LatLng(43.3, 21.9))));
//        grid.addItem(new GridItem(1, 3, dashboardViewInitializer, new DashboardData(DashboardData.BAR_CHART, R.drawable.ic_directions_walk, data)));
//        grid.addItem(new GridItem(1, 1, dashboardViewInitializer, new DashboardData(DashboardData.COUNT, R.drawable.ic_directions_walk, "5000/10000")));
//        grid.addItem(new GridItem(1, 1, dashboardViewInitializer, new DashboardData(DashboardData.COUNT, R.drawable.ic_phone_in_talk_black_48dp, "In: 6\nOut: 2\nMissed: 3")));
//        grid.addItem(new GridItem(1, 1, dashboardViewInitializer, new DashboardData(DashboardData.COUNT, R.drawable.ic_sms_black_48dp, "In: 15\nOut: 23")));

        grid.layoutItems(inflater);

        return scrollView;
    }
}
