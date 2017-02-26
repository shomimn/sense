package com.mnm.sense.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mnm.sense.R;
import com.mnm.sense.SenseApp;
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
            BitmapDescriptor icon = makeIcon(attrs);

            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(icon)
            );
        }
    }

    private BitmapDescriptor makeIcon(ArrayList<AttributedFeature> features)
    {
        if (features.size() == 1)
            return BitmapDescriptorFactory.fromBitmap(features.get(0).icon());

        final int max = 3;

        int size = Util.dp(100);
        int segmentSize = size / 4;
        Bitmap bitmap = Bitmap.createBitmap(size, segmentSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        boolean drawMore = features.size() > max;

        for (int i = 0; i < features.size(); ++i)
        {
            if (i == max)
                break;

            AttributedFeature feature = features.get(i);
            Bitmap icon = feature.icon();

            drawTranslate(canvas, icon, segmentSize, segmentSize, 0);
        }

        if (drawMore)
        {
            Bitmap more = Util.bitmapFromResource(SenseApp.context(), R.drawable.ic_more_horiz_black_48dp,
                    SenseApp.context().getResources().getColor(android.R.color.black));

            drawTranslate(canvas, more, segmentSize, segmentSize, 0);
        }

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void drawTranslate(Canvas canvas, Bitmap bitmap, int scaledSize, int dx, int dy)
    {
        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect dst = new Rect(0, 0, scaledSize, scaledSize);

        canvas.drawBitmap(bitmap, src, dst, null);
        canvas.translate(dx, dy);
    }
}
