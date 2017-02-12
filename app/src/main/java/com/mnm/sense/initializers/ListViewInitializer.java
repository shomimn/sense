package com.mnm.sense.initializers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mnm.sense.R;
import com.mnm.sense.Util;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.mnm.sense.models.ListViewData;
import com.mnm.sense.models.ListViewModel;
import com.mnm.sense.trackers.Tracker;
import com.ubhave.sensormanager.data.SensorData;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class ListViewInitializer extends ViewInitializer<ListView, ListViewModel>
{
    public class ListViewAdapter extends ArrayAdapter<String>
    {
        private Context context;
        private ListViewData data;

        public ListViewAdapter(Context context, ListViewData data) {
            super(context, R.layout.list_item, data.getNames());
            this.context = context;
            this.data = data;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            View rowView= inflater.inflate(R.layout.list_item, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.item_text);

            ImageView imageView = (ImageView) rowView.findViewById(R.id.item_image);
            txtTitle.setText(data.getNameAt(position));
            imageView.setImageDrawable(data.getIconAt(position));
            return rowView;
        }
    }

    @TargetApi(17)
    public class GridViewAdapter extends ArrayAdapter<Integer>
    {
        private Context context;
        private ListViewData data;
        private int counter = 0;
        public GridViewAdapter(Context context, ListViewData data) {
            super(context, R.layout.grid_images, new Integer[3]);
            this.context = context;
            this.data = data;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            LinearLayout gridView = (LinearLayout) inflater.inflate(R.layout.grid_images, null, false);


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            int i = 0;
            while(i < 5 && counter < data.getIcons().length)
            {
                LinearLayout gridItem = (LinearLayout) inflater.inflate(R.layout.list_item, null, false);
                ImageView image = (ImageView)gridItem.findViewById(R.id.item_image);
                TextView text = (TextView)gridItem.findViewById(R.id.item_text);

                gridItem.setOrientation(LinearLayout.VERTICAL);
                gridItem.setPadding(15, 15, 15, 15);
                text.setTextSize(13);
                text.setText(data.getNameAt(counter));

                image.setImageDrawable(data.getIconAt(counter++));

                gridView.addView(gridItem);
                i++;
            }

            return gridView;
        }
    }

    public ListViewInitializer()
    {
        super(ListView.class, ListViewModel.class, Visualization.LIST_VIEW);
    }

    @Override
    public void init(final Context context, final ListView view, final ListViewModel model)
    {
        final AppCompatActivity activity = (AppCompatActivity) context;
        final VisualizationAdapter adapter = model.tracker.defaultAdapter(visualization);

        if (model.shouldUpdate)
        {
            view.setAdapter(new GridViewAdapter(context, model.data));

            model.tracker.updateCallbacks.put(visualization, new Tracker.UpdateCallback()
            {
                @Override
                public void update(ArrayList<SensorData> with)
                {
                    final ListViewData data = (ListViewData) adapter.adapt(with);

                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            view.setAdapter(new GridViewAdapter(context, data));
                            view.invalidate();
                        }
                    });
                }
            });
        }
        else
        {
            view.setAdapter(new ListViewAdapter(context, model.data));
        }
    }
}
