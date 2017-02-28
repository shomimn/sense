package com.mnm.sense.map;

import java.util.ArrayList;

public class SenseOverlay
{
    public enum Type
    {
        NONE,
        HEATMAP
    }

    protected Type type;
    protected ArrayList<AttributedFeature> features;

    private SenseOverlay(Type t, ArrayList<AttributedFeature> p)
    {
        type = t;
        features = p;
    }

    public Type type()
    {
        return type;
    }

    public ArrayList<AttributedFeature> points()
    {
        return features;
    }

    public static SenseOverlay heatmap(ArrayList<AttributedFeature> p)
    {
        return new SenseOverlay(Type.HEATMAP, p);
    }
}
