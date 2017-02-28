package com.mnm.sense.map;

public class Geometry
{
    public enum Type
    {
        POINT,
        POLYLINE,
        POLYGON
    }

    protected Type type;
    protected Points points;

    Geometry(Type t, Points p)
    {
        type = t;
        points = p;
    }

    public Type type()
    {
        return type;
    }

    public Points points()
    {
        return points;
    }
}
