package com.mnm.sense.map;

public class SensePolyline extends Geometry
{
    private SensePolyline(Points p)
    {
        super(Type.POLYLINE, p);
    }

    public static SensePolyline make(Points p)
    {
        return new SensePolyline(p);
    }
}
