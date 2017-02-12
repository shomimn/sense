package com.mnm.sense;


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
import android.support.v4.widget.CompoundButtonCompat;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;


public class Util
{
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

    public static void setAccent(TextView view, int accent)
    {
        view.setTextColor(view.getResources().getColor(accent));
    }

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

    public static void setAccent(Switch view, int accent)
    {
        int color = view.getResources().getColor(accent);
        int unchecked = view.getResources().getColor(android.R.color.darker_gray);

        int buttonStates[][] = { { -android.R.attr.state_checked }, { android.R.attr.state_checked } };
        int buttonColors[] = { android.R.color.darker_gray, color };

        int thumbStates[][] = { { -android.R.attr.state_checked}, { android.R.attr.state_checked } };
        int thumbColors[] = { unchecked, color };

//        view.setButtonTintList(new ColorStateList(buttonStates, buttonColors));
        view.setThumbTintList(new ColorStateList(thumbStates, thumbColors));


//        CompoundButtonCompat.setButtonTintList(view, new ColorStateList(buttonStates, buttonColors));
    }
}
