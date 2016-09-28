package com.mnm.sense;


import android.content.Context;
import android.widget.ImageView;

public class ImageViewInitializer extends ViewInitializer<ImageView, Integer>
{
    public ImageViewInitializer()
    {
        super(ImageView.class);
    }

    @Override
    public void init(Context context, ImageView view, Integer data)
    {
        view.setImageResource(data);
    }
}
