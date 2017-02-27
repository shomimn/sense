package com.mnm.sense;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.CompoundButtonCompat;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.ubhave.sensormanager.data.SensorData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;


public class Util
{
    public interface Predicate<T>
    {
        public boolean test(T data);
    }

    public static int dp(int pixels)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, SenseApp.context().getResources().getDisplayMetrics());
    }

    public static Bitmap bitmapFromResource(Context context, int resource, int color)
    {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resource);
        Bitmap coloredBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(coloredBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(bitmap, 0f, 0f, paint);

        return coloredBitmap;
    }

    public static Bitmap bitmapFromResource(int resource, int colorResource)
    {
        return bitmapFromResource(SenseApp.context(), resource,
                SenseApp.context().getResources().getColor(colorResource));
    }

    public static Bitmap bitmapFromResource(int resource)
    {
        return BitmapFactory.decodeResource(SenseApp.context().getResources(), resource);
    }

    public static Bitmap drawableToBitmap(Drawable drawable)
    {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable)
        {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null)
                return bitmapDrawable.getBitmap();
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0)
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        else
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    public static void setAccent(TextView view, int accent)
    {
        view.setTextColor(view.getResources().getColor(accent));
    }

    @TargetApi(23)
    public static void setAccent(Button view, int accent)
    {
        int color = view.getResources().getColor(accent);

        int states[][] = { { android.R.attr.state_pressed, android.R.attr.state_focused, android.R.attr.state_long_pressable } };
        int colors[] = { color };

        view.setBackgroundTintList(new ColorStateList(states, colors));
        view.setForegroundTintList(new ColorStateList(states, colors));
        view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        view.setTextColor(color);
    }

    @TargetApi(23)
    public static void setAccent(Switch view, int accent)
    {
        if (android.os.Build.VERSION.SDK_INT >= 23)
        {
            int checked = view.getResources().getColor(accent);
            int unchecked = Color.rgb(236, 236, 236);
            int disabled = Color.rgb(185, 185, 185);

            int buttonStates[][] = { { -android.R.attr.state_checked }, { android.R.attr.state_checked } };
            int buttonColors[] = { disabled, checked };

            int thumbStates[][] = { { -android.R.attr.state_checked}, { android.R.attr.state_checked } };
            int thumbColors[] = { unchecked, checked };

            view.setThumbTintList(new ColorStateList(thumbStates, thumbColors));
            view.setTrackTintList(new ColorStateList(buttonStates, buttonColors));
        }
    }

    public static ArrayList<SensorData> filter(ArrayList<SensorData> list, Predicate<SensorData> predicate)
    {
        ArrayList<SensorData> result = new ArrayList<>();

        for (SensorData sensorData : list)
            if (predicate.test(sensorData))
                result.add(sensorData);

        return result;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map )
    {
        ArrayList<Map.Entry<K, V>> list =
                new ArrayList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o1.getValue()).compareTo( o2.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
            result.put( entry.getKey(), entry.getValue() );

        return result;
    }

    public static long today()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTimeInMillis();
    }
}
