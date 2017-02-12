package com.mnm.sense.initializers;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.mnm.sense.R;
import com.mnm.sense.Util;
import com.mnm.sense.Visualization;
import com.mnm.sense.models.MapModel;
import com.mnm.sense.trackers.LocationTracker;
import com.mnm.sense.trackers.Tracker;
import com.ubhave.sensormanager.data.SensorData;

import java.util.ArrayList;

public class MapFragmentInitializer extends ViewInitializer<SupportMapFragment, MapModel>
{
    MapFragmentInitializer()
    {
        super(SupportMapFragment.class, MapModel.class, Visualization.MAP);
    }

    @Override
    public SupportMapFragment construct(Context context, MapModel model)
    {
        SupportMapFragment mapFragment = new SupportMapFragment();
        init(context, mapFragment, model);

        return mapFragment;
    }

    @Override
    public void init(final Context context, final SupportMapFragment view, final MapModel model)
    {
        view.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(final GoogleMap googleMap)
            {
                googleMap.getUiSettings().setScrollGesturesEnabled(model.scrollEnabled);

                final Tracker tracker = model.tracker;
                final ArrayList<Marker> markers = new ArrayList<>();
                final PolylineOptions polylineOptions = new PolylineOptions();
                final LatLngBounds.Builder builder = new LatLngBounds.Builder();

                googleMap.clear();

                switch (model.attribute)
                {
                    case LocationTracker.ATTRIBUTE_MARKER:
                        for (LatLng location : model.data)
                        {
                            builder.include(location);
                            Marker marker = googleMap.addMarker(
                                    new MarkerOptions()
                                            .position(location)
                            );

                            markers.add(marker);
                        }
                        break;
                    case LocationTracker.ATTRIBUTE_PATH:
                        Bitmap arrow = Util.bitmapFromResource(context, R.drawable.ic_navigation_black_24dp, Color.RED);
//                        for (LatLng location : model.data)
                        for (int i = 0; i < model.data.size(); ++i)
                        {
                            LatLng location = model.data.get(i);

                            builder.include(location);
                            polylineOptions.add(location);

                            if (i == model.data.size() - 1)
                                continue;

                            LatLng nextLocation = model.data.get(i + 1);

                            if (SphericalUtil.computeDistanceBetween(location, nextLocation) < 10)
                                continue;

                            float heading = (float) SphericalUtil.computeHeading(location, nextLocation);

                            googleMap.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromBitmap(arrow)).anchor(0.5f, 0.5f).rotation(heading));
                        }
                        googleMap.addPolyline(polylineOptions);
                        break;
                }

                if (model.data.size() > 0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 15));

                if (model.shouldUpdate)
                {
                    tracker.updateCallbacks.put(visualization, new Tracker.UpdateCallback()
                    {
                        @Override
                        public void update(ArrayList<SensorData> with)
                        {
                            final AppCompatActivity activity = (AppCompatActivity) context;
                            final LatLng latLng = (LatLng) tracker.adapter(model.attribute, visualization).adaptOne(with.get(with.size() - 1));

                            builder.include(latLng);

                            activity.runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Log.d("SENSE", "Updating map with " + latLng.toString());

                                    switch (model.attribute)
                                    {
                                        case LocationTracker.ATTRIBUTE_MARKER:
                                            markers.add(googleMap.addMarker(new MarkerOptions().position(latLng)));
                                            break;
                                        case LocationTracker.ATTRIBUTE_PATH:
                                            googleMap.clear();
                                            polylineOptions.add(latLng);
                                            googleMap.addPolyline(polylineOptions);
                                            break;
                                    }

                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 15));
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    @Override
    public <Y> SupportMapFragment injectIn(Context context, Y parent, MapModel model)
    {
        View parentView = (View) parent;
        LinearLayout viewGroup = (LinearLayout) parentView.findViewById(R.id.layout);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewGroup.getLayoutParams();
//        params.setMargins(Util.dp(5), 0, 0, 0);

        SupportMapFragment mapFragment = construct(context, model);
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(viewGroup.getId(), mapFragment);
        transaction.commit();

        return mapFragment;
    }
}
