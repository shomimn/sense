package com.mnm.sense.map;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class AttributedFeature
{
    public enum GeometryType
    {
        POINT,
        POLYLINE,
        POLYGON
    }

    public static abstract class Geometry
    {
        protected GeometryType type;
        protected Points points;

        public Geometry(GeometryType t, Points p)
        {
            type = t;
            points = p;
        }

        public GeometryType type()
        {
            return type;
        }

        public Points points()
        {
            return points;
        }
    }

    public static class SensePoint extends Geometry
    {
        private SensePoint(Points p)
        {
            super(GeometryType.POINT, p);
        }

        public SensePoint make(LatLng point)
        {
            Points points = new Points();
            points.add(point);

            return new SensePoint(points);
        }
    }

    public static class SensePolyline extends Geometry
    {
        private SensePolyline(Points p)
        {
            super(GeometryType.POLYLINE, p);
        }

        public SensePolyline make(Points p)
        {
            return new SensePolyline(p);
        }
    }

    private int origin;
    private Bitmap icon;
    private String text;
    private LatLng latLng;
    private LinkedHashMap<String, String> customAttributes = new LinkedHashMap<>();
    private Bitmap image;
    private Geometry geometry;

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

    public Geometry geometry()
    {
        return geometry;
    }

    public AttributedFeature geometry(Geometry geometry)
    {
        this.geometry = geometry;

        return this;
    }
}
