package com.mnm.sense.initializers;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mnm.sense.R;
import com.mnm.sense.Visualization;
import com.mnm.sense.adapters.VisualizationAdapter;
import com.mnm.sense.models.ListViewData;
import com.mnm.sense.models.ListViewModel;
import com.mnm.sense.trackers.Tracker;
import com.ubhave.sensormanager.data.SensorData;

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

    public ListViewInitializer()
    {
        super(ListView.class, ListViewModel.class, Visualization.LIST_VIEW);
    }

    @Override
    public void init(final Context context, final ListView view, final ListViewModel model)
    {
        final AppCompatActivity activity = (AppCompatActivity) context;
        final VisualizationAdapter adapter = model.tracker.defaultAdapter(visualization);
        view.setAdapter(new ListViewAdapter(context, model.data));

        if (model.shouldUpdate)
        {
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
                            view.setAdapter(new ListViewAdapter(context, data));
                            view.invalidate();
                        }
                    });
                }
            });
        }
    }
}
