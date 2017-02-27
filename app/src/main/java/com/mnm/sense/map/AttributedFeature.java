package com.mnm.sense.map;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class AttributedFeature
{
    private int origin;
    private Bitmap icon;
    private String text;
    private LatLng latLng;
    private LinkedHashMap<String, String> customAttributes = new LinkedHashMap<>();
    private Bitmap image;

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

    public LatLng latLng()
    {
        return latLng;
    }

    public AttributedFeature latLng(LatLng latLng)
    {
        this.latLng = latLng;

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
}
