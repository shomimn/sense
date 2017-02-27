package com.mnm.sense.map;

import com.google.android.gms.maps.model.LatLng;

public class SensePoint extends Geometry
{
    private SensePoint(Points p)
    {
        super(Type.POINT, p);
    }

    public static SensePoint make(LatLng point)
    {
        Points points = new Points();
        points.add(point);

        return new SensePoint(points);
    }
}
