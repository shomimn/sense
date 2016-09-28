package com.mnm.sense;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragmentInitializer extends ViewInitializer<SupportMapFragment, LatLng>
{
    MapFragmentInitializer()
    {
        super(SupportMapFragment.class);
    }

    @Override
    public SupportMapFragment construct(Context context, LatLng data)
    {
        SupportMapFragment mapFragment = new SupportMapFragment();
        init(context, mapFragment, data);

        return mapFragment;
    }

    @Override
    public void init(Context context, SupportMapFragment view, final LatLng data)
    {
        view.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(GoogleMap googleMap)
            {
                googleMap.addMarker(new MarkerOptions().position(data));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(data, 10));
            }
        });
    }

    @Override
    public <Y> void injectIn(Context context, Y parent, LatLng data)
    {
        View parentView = (View) parent;
        LinearLayout viewGroup = (LinearLayout) parentView.findViewById(R.id.layout);

        SupportMapFragment mapFragment = construct(context, data);
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(viewGroup.getId(), mapFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
