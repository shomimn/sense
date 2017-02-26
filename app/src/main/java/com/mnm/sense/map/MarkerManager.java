package com.mnm.sense.map;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Window;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.mnm.sense.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MarkerManager implements GoogleMap.OnMarkerClickListener
{
    private Context context;
    private HashMap<LatLng, ArrayList<AttributedFeature>> markers = new HashMap<>();

    public MarkerManager(Context c)
    {
        context = c;
    }

    public void add(AttributedFeature attr)
    {
        LatLng latLng = attr.latLng();

        if (markers.get(latLng) == null)
            markers.put(latLng, new ArrayList<AttributedFeature>());

        markers.get(latLng).add(attr);
    }

    public void clear()
    {
        markers.clear();
    }

    public ArrayList<AttributedFeature> getMarkersAt(LatLng latLng)
    {
        return markers.get(latLng);
    }

    public HashMap<LatLng, ArrayList<AttributedFeature>> getMarkers()
    {
        return markers;
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.marker_info);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((7 * width) / 8, height / 2);

        ViewPager viewPager = (ViewPager) dialog.findViewById(R.id.marker_info);
        viewPager.setAdapter(new MarkerInfoAdapter(context, markers.get(marker.getPosition())));

        dialog.show();

        return true;
    }
}
