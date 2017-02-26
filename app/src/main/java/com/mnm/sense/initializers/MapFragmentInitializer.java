package com.mnm.sense.initializers;

import android.content.Context;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.mnm.sense.map.AttributedFeature;
import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.map.MarkerManager;
import com.mnm.sense.map.SenseMapRenderer;
import com.mnm.sense.models.MapModel;
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

                final AppCompatActivity activity = (AppCompatActivity) context;
                final Tracker tracker = model.tracker;

                final MarkerManager markerManager = new MarkerManager(context);
                final SenseMapRenderer renderer = new SenseMapRenderer(markerManager, googleMap);
                final PolylineOptions polylineOptions = new PolylineOptions();
                final LatLngBounds.Builder builder = new LatLngBounds.Builder();
                final HeatmapTileProvider.Builder heatmapBuilder = new HeatmapTileProvider.Builder();

                googleMap.clear();

                for (AttributedFeature attr : model.data)
                {
                    renderer.include(attr);
                }

                renderer.render();

                googleMap.setOnMarkerClickListener(markerManager);

                if (model.data.size() > 0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(renderer.envelope(), 15));

                model.shouldUpdate = false;
                if (model.shouldUpdate)
                {
                    tracker.updateCallbacks.put(visualization, new Tracker.UpdateCallback()
                    {
                        @Override
                        public void update(ArrayList<SensorData> with)
                        {
                            final LatLng latLng = (LatLng) tracker.adapter(model.attribute, visualization).adaptOne(with.get(with.size() - 1));

                            builder.include(latLng);

                            activity.runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Log.d("SENSE", "Updating map with " + latLng.toString());

//                                    markers.add(googleMap.addMarker(new MarkerOptions().position(latLng)));

                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 15));
                                }
                            });
                        }
                    });

                    tracker.clearCallbacks.put(visualization, new Tracker.ClearCallback()
                    {
                        @Override
                        public void clear()
                        {
                            googleMap.clear();
                            markerManager.clear();
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