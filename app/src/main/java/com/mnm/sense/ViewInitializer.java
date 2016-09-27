package com.mnm.sense;


import android.content.Context;

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
            init(view, data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return view;
    }

    public abstract void init(T view, U data);
}
