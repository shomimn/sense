package com.mnm.sense.initializers;


import android.content.Context;
import android.widget.ImageView;

public class ImageViewInitializer extends ViewInitializer<ImageView, Integer>
{
    public ImageViewInitializer()
    {
        super(ImageView.class, Integer.class);
    }

    @Override
    public void init(Context context, ImageView view, Integer model)
    {
        view.setImageResource(model);
    }
}
