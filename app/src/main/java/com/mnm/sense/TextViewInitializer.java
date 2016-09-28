package com.mnm.sense;


import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

public class TextViewInitializer extends ViewInitializer<TextView, String>
{
    public TextViewInitializer()
    {
        super(TextView.class);
    }

    @Override
    public void init(Context context, TextView view, String data)
    {
        view.setText(data);
        view.setGravity(Gravity.CENTER);
        view.setTextColor(context.getResources().getColor(R.color.colorAccent));
    }
}
