package com.mnm.sense.map;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mnm.sense.R;

import java.util.ArrayList;
import java.util.Map;

public class MarkerInfoAdapter extends PagerAdapter
{
    private Context context;
    private ArrayList<AttributedFeature> markerInfo;

    public MarkerInfoAdapter(Context c, ArrayList<AttributedFeature> info)
    {
        context = c;
        markerInfo = info;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        AttributedFeature attr = markerInfo.get(position);
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.marker_info_item, container, false);

        populateSubviews(view, attr);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((View) object);
    }

    @Override
    public int getCount()
    {
        return markerInfo.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    public void populateSubviews(View view, AttributedFeature attr)
    {
        ImageView origin = (ImageView) view.findViewById(R.id.marker_info_image);
        TextView textView = (TextView) view.findViewById(R.id.marker_info_text);
        LinearLayout innerLayout = (LinearLayout) view.findViewById(R.id.inner_layout);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);

        origin.setImageResource(attr.origin());
        textView.setText(attr.text());

        for (Map.Entry<String, String> entry : attr.custom().entrySet())
        {
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            TextView keyView = new TextView(context);
            keyView.setTypeface(null, Typeface.BOLD);
            keyView.setText(entry.getKey());
//            keyView.setTextColor(colorAccent);
            keyView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            keyView.setGravity(Gravity.CENTER_VERTICAL);

            TextView valueView = new TextView(context);
            valueView.setText(entry.getValue());
//            valueView.setTextColor(colorAccent);
            valueView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            valueView.setGravity(Gravity.CENTER_VERTICAL);

            layout.addView(keyView, textParams);
            layout.addView(valueView, textParams);

            innerLayout.addView(layout);
        }

        if (attr.hasImage())
        {
            ImageView imageView = new ImageView(context);
            imageView.setImageBitmap(attr.image());

            innerLayout.addView(imageView, imageParams);
        }
    }
}
