package com.mnm.sense.map;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.mnm.sense.SenseApp;

import java.util.HashMap;
import java.util.LinkedHashMap;


public class AttributedFeature
{
    private int origin;
    private Bitmap icon;
    private String text;
//    private LatLng latLng;
    private LinkedHashMap<String, String> customAttributes = new LinkedHashMap<>();
    private Bitmap image;
    private Geometry geometry;
    private int accent;

    public Bitmap icon()
    {
        return icon;
    }

    public AttributedFeature icon(Bitmap icon)
    {
        this.icon = icon;
        return this;
    }

    public String text()
    {
        return text;
    }

    public AttributedFeature text(String text)
    {
        this.text = text;

        return this;
    }

    public int origin()
    {
        return origin;
    }

    public AttributedFeature origin(int origin)
    {
        this.origin = origin;

        return this;
    }

    public HashMap<String, String> custom()
    {
        return customAttributes;
    }

    public AttributedFeature custom(String key, String value)
    {
        customAttributes.put(key, value);

        return this;
    }

    public Bitmap image()
    {
        return image;
    }

    public AttributedFeature image(Bitmap image)
    {
        this.image = image;

        return this;
    }

    public boolean hasImage()
    {
        return image != null;
    }

    public Geometry geometry()
    {
        return geometry;
    }

    public AttributedFeature geometry(Geometry geometry)
    {
        this.geometry = geometry;

        return this;
    }

    public int accent()
    {
        return accent;
    }

    public AttributedFeature accent(int color)
    {
        accent = SenseApp.context().getResources().getColor(color);

        return this;
    }

    public boolean hasAccent()
    {
        return accent != 0;
    }
}
