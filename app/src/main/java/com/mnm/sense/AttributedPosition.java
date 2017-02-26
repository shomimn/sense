package com.mnm.sense;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AttributedPosition
{
    private int origin;
    private int icon;
    private String text;
    private LatLng latLng;
    private LinkedHashMap<String, String> customAttributes = new LinkedHashMap<>();

    public int icon()
    {
        return icon;
    }

    public AttributedPosition icon(int icon)
    {
        this.icon = icon;

        return this;
    }

    public String text()
    {
        return text;
    }

    public AttributedPosition text(String text)
    {
        this.text = text;

        return this;
    }

    public LatLng latLng()
    {
        return latLng;
    }

    public AttributedPosition latLng(LatLng latLng)
    {
        this.latLng = latLng;

        return this;
    }

    public int origin()
    {
        return origin;
    }

    public AttributedPosition origin(int origin)
    {
        this.origin = origin;

        return this;
    }

    public HashMap<String, String> custom()
    {
        return customAttributes;
    }

    public AttributedPosition custom(String key, String value)
    {
        customAttributes.put(key, value);

        return this;
    }
}
