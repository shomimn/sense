package com.mnm.sense.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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

    public void renderMarker(AttributedPosition attr)
    {
        LatLng latLng = attr.latLng();

        boundsBuilder.include(latLng);
        Marker marker = googleMap.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .title(attr.text())
        );

        markerManager.add(marker, attr);
    }
}
