package com.mnm.sense.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mnm.sense.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SenseMapRenderer
{
    private LatLngBounds.Builder boundsBuilder;
    private MarkerManager markerManager;
    private GoogleMap googleMap;

    public SenseMapRenderer(MarkerManager manager, GoogleMap map)
    {
        boundsBuilder = new LatLngBounds.Builder();
        markerManager = manager;
        googleMap = map;
    }

    public LatLngBounds envelope()
    {
        return boundsBuilder.build();
    }

    public void include(AttributedFeature attr)
    {
        boundsBuilder.include(attr.latLng());
        markerManager.add(attr);
    }

    public void render()
    {
        HashMap<LatLng, ArrayList<AttributedFeature>> markers = markerManager.getMarkers();

        for (Map.Entry<LatLng, ArrayList<AttributedFeature>> entry : markers.entrySet())
        {
            LatLng latLng = entry.getKey();
            ArrayList<AttributedFeature> attrs = entry.getValue();

            if (attrs.size() == 1)
            {
                AttributedFeature feature = attrs.get(0);

                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(feature.icon())));
            }
            else
            {
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(combinedIcon(attrs)));
            }
        }
    }

    private BitmapDescriptor combinedIcon(ArrayList<AttributedFeature> features)
    {
        int size = Util.dp(100);
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        int x = 0;
        int y = 0;
        int offset = size / 4;

        for (AttributedFeature feature : features)
        {
            Log.d("CombinedIcon", String.format("x: %d", x));
            Bitmap icon = feature.icon();
            Rect src = new Rect(0, 0, icon.getWidth(), icon.getHeight());
            Rect dst = new Rect(0, 0, offset, offset);

            canvas.translate(offset, 0);
            canvas.drawBitmap(feature.icon(), src, dst, null);

            x += offset;
        }

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
