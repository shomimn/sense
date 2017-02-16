package com.mnm.sense.initializers;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.models.BaseModel;

import java.lang.reflect.Constructor;

public abstract class ViewInitializer<T, U>
{
    protected String visualization;
    protected Class viewClass;
    protected Class dataClass;

    public ViewInitializer(Class aClass, Class dClass)
    {
        viewClass = aClass;
        dataClass = dClass;
        visualization = Visualization.NONE;
    }

    public ViewInitializer(Class aClass, Class dClass, String v)
    {
        viewClass = aClass;
        dataClass = dClass;
        visualization = v;
    }

    public T construct(Context context, U model)
    {
        T view = null;

        try
        {
            Constructor constructor = viewClass.getConstructor(Context.class);
            view = (T) constructor.newInstance(context);
            init(context, view, model);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return view;
    }

    public View construct(Context context, int layout, U model)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layout, null, false);
        init(context, (T) view, model);

        return view;
    }

    public <Y> T injectIn(Context context, Y parent, U model)
    {
        View parentView = (View) parent;
        LinearLayout viewGroup = (LinearLayout) parentView.findViewById(R.id.layout);

        T view = construct(context, model);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;

        viewGroup.addView((View) view, params);

        return view;
    }

    public abstract void init(Context context, T view, U model);
}
