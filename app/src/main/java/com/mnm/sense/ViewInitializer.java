package com.mnm.sense;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.reflect.Constructor;

public abstract class ViewInitializer<T, U>
{
    protected Class viewClass;

    public ViewInitializer(Class aClass)
    {
        viewClass = aClass;
    }

    public T construct(Context context, U data)
    {
        T view = null;

        try
        {
            Constructor constructor = viewClass.getConstructor(Context.class);
            view = (T) constructor.newInstance(context);
            init(context, view, data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return view;
    }

    public View construct(Context context, int layout, U data)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layout, null, false);
        init(context, (T) view, data);

        return view;
    }

    public <Y> void injectIn(Context context, Y parent, U data)
    {
        View parentView = (View) parent;
        LinearLayout viewGroup = (LinearLayout) parentView.findViewById(R.id.layout);

        T view = construct(context, data);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;

        viewGroup.addView((View) view, params);
    }

    public abstract void init(Context context, T view, U data);
}
