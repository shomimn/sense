package com.mnm.sense;


import android.widget.ImageView;

public class ImageViewInitializer extends ViewInitializer<ImageView, Integer>
{
    public ImageViewInitializer()
    {
        super(ImageView.class);
    }

    @Override
    public void init(ImageView view, Integer data)
    {
        view.setImageResource(data);
        view.setColorFilter(SenseApp.context().getResources().getColor(R.color.colorAccent));
    }
}
