package com.mnm.sense.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.mnm.sense.R;
import com.mnm.sense.SenseApp;
import com.mnm.sense.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SenseMapRenderer
{
    private LatLngBounds.Builder boundsBuilder;
    private HeatmapTileProvider.Builder heatmapProvider;
    private MarkerManager markerManager;
    private LineManager lineManager;
    private GoogleMap googleMap;

    public SenseMapRenderer(MarkerManager mManager, LineManager lManager, GoogleMap map)
    {
        heatmapProvider = new HeatmapTileProvider.Builder();
        boundsBuilder = new LatLngBounds.Builder();
        markerManager = mManager;
        lineManager = lManager;
        googleMap = map;
    }

    public LatLngBounds envelope()
    {
        return boundsBuilder.build();
    }

    public void include(AttributedFeature attr)
    {
        for (LatLng point : attr.geometry().points)
            boundsBuilder.include(point);

        switch (attr.geometry().type())
        {
            case POINT:
                markerManager.add(attr);

                break;
            case POLYLINE:
                lineManager.add(attr);

                break;
            case POLYGON:
                break;
        }
    }

    public void render()
    {
        ArrayList<AttributedFeature> lines = lineManager.getLines();

        for (AttributedFeature line : lines)
        {
//            markerManager.add(line);

            googleMap.addPolyline(new PolylineOptions()
                    .addAll(line.geometry().points)
                    .color(line.accent()));
        }

        HashMap<LatLng, ArrayList<AttributedFeature>> markers = markerManager.getMarkers();

        for (Map.Entry<LatLng, ArrayList<AttributedFeature>> entry : markers.entrySet())
        {
            LatLng latLng = entry.getKey();
            ArrayList<AttributedFeature> attrs = entry.getValue();
            BitmapDescriptor icon = makeIcon(attrs);

            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(icon)
                    .anchor(0.5f, 0.5f)
            );
        }
    }

    private BitmapDescriptor makeIcon(ArrayList<AttributedFeature> features)
    {
        final int max = 3;

        int segmentSize = Util.dp(100) / (max + 1);

        int size = segmentSize * features.size();

        Bitmap bitmap = Bitmap.createBitmap(size, segmentSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        boolean drawMore = features.size() > max;

        for (int i = 0; i < features.size(); ++i)
        {
            if (i == max)
                break;

            AttributedFeature feature = features.get(i);
            Bitmap icon = roundedCornerBitmap(feature.icon(), segmentSize, segmentSize, segmentSize / 5);

//            Paint white = new Paint();
//            white.setColor(Color.WHITE);
//            white.setStyle(Paint.Style.FILL);

//            canvas.drawRoundRect(0, 0, segmentSize, segmentSize, segmentSize / 5, segmentSize / 5, white);

            Paint paint = null;

            if (feature.hasAccent())
            {
                paint = new Paint();
                paint.setColorFilter(new PorterDuffColorFilter(feature.accent(), PorterDuff.Mode.SRC_ATOP));
            }

            drawTranslate(canvas, icon, paint, segmentSize, segmentSize, 0);
        }

        if (drawMore)
        {
            TextPaint textPaint = new TextPaint();
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(10 * SenseApp.context().getResources().getDisplayMetrics().density);
            textPaint.setColor(0xFF000000);

            String plus = "+" + String.valueOf(features.size() - max);

            int width = (int) textPaint.measureText(plus);

            StaticLayout staticLayout = new StaticLayout(plus, textPaint, width, Layout.Alignment.ALIGN_CENTER, 1.0f, 0, false);
            staticLayout.draw(canvas);
        }

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void drawTranslate(Canvas canvas, Bitmap bitmap, Paint paint, int scaledSize, int dx, int dy)
    {
        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect dst = new Rect(0, 0, scaledSize, scaledSize);

        canvas.drawBitmap(bitmap, src, dst, paint);
        canvas.translate(dx, dy);
    }

    private Bitmap roundedCornerBitmap(Bitmap bitmap, int width, int height, int pixels)
    {
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(0, 0, width, height);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);

        return output;
    }
}
