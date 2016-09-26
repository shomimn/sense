package com.mnm.sense;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Utils;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.maps.MapsInitializer;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.mit.media.funf.FunfManager;
import edu.mit.media.funf.json.IJsonObject;
import edu.mit.media.funf.pipeline.BasicPipeline;
import edu.mit.media.funf.probe.Probe;
import edu.mit.media.funf.probe.builtin.AccountsProbe;
import edu.mit.media.funf.probe.builtin.ApplicationsProbe;
import edu.mit.media.funf.probe.builtin.BatteryProbe;
import edu.mit.media.funf.probe.builtin.BrowserSearchesProbe;
import edu.mit.media.funf.probe.builtin.CallLogProbe;
import edu.mit.media.funf.probe.builtin.ContactProbe;
import edu.mit.media.funf.probe.builtin.RunningApplicationsProbe;
import edu.mit.media.funf.probe.builtin.ScreenProbe;
import edu.mit.media.funf.probe.builtin.SimpleLocationProbe;
import edu.mit.media.funf.probe.builtin.TelephonyProbe;
import edu.mit.media.funf.probe.builtin.WifiProbe;
import edu.mit.media.funf.storage.NameValueDatabaseHelper;

import org.sensingkit.sensingkitlib.*;
import org.sensingkit.sensingkitlib.data.SKAmbientTemperatureData;
import org.sensingkit.sensingkitlib.data.SKHumidityData;
import org.sensingkit.sensingkitlib.data.SKMotionActivityData;
import org.sensingkit.sensingkitlib.data.SKSensorData;
import org.sensingkit.sensingkitlib.data.SKStepCounterData;
import org.sensingkit.sensingkitlib.data.SKStepDetectorData;

import com.github.mikephil.charting.charts.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.support.v7.widget.GridLayout;

public class MainActivity extends AppCompatActivity implements Probe.DataListener
{
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

    public class BarChartInitializer extends ViewInitializer<BarChart, BarData>
    {
        public BarChartInitializer()
        {
            super(BarChart.class);
        }

        @Override
        public void init(BarChart barChart, BarData data)
        {
            barChart.setData(data);
            barChart.fitScreen();
            barChart.setDescription(null);
            barChart.setDrawBorders(false);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);

            YAxis leftYAxis = barChart.getAxisLeft();
            leftYAxis.setDrawGridLines(false);
            leftYAxis.setDrawLabels(false);
            leftYAxis.setDrawAxisLine(false);

            YAxis rightYAxis = barChart.getAxisRight();
            rightYAxis.setDrawGridLines(false);
            rightYAxis.setDrawLabels(false);
            rightYAxis.setDrawAxisLine(false);
        }
    }

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
        }
    }

    public class DashboardItem
    {
        public int columnSpan;
        ViewInitializer initializer;
        Object data;

        public DashboardItem(int span, ViewInitializer init, Object d)
        {
            columnSpan = span;
            initializer = init;
            data = d;
        }
    }

    int dp(int pixels)
    {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, getResources().getDisplayMetrics());
    }

    public static final String PIPELINE_NAME = "default";

    List<BarEntry> entries = new ArrayList<>(25);
    List<String> vals = new ArrayList<>(25);
    Random random = new Random();

    GridLayout gridLayout;

    ArrayList<DashboardItem> items;
    BarChartInitializer barChartInitializer = new BarChartInitializer();
    ImageViewInitializer imageViewInitializer = new ImageViewInitializer();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        for (int i = 0; i < 25; ++i)
        {
            entries.add(new BarEntry(i, random.nextFloat()));
            vals.add(String.valueOf(i));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Steps");
        dataSet.setColor(Color.parseColor("#43A047"));
        final BarData data = new BarData(dataSet);
        data.setBarWidth(0.2f);

        items = new ArrayList<>();
        items.add(new DashboardItem(3, barChartInitializer, data));
        items.add(new DashboardItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new DashboardItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new DashboardItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new DashboardItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new DashboardItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new DashboardItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new DashboardItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new DashboardItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new DashboardItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new DashboardItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new DashboardItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new DashboardItem(1, imageViewInitializer, R.mipmap.ic_launcher));
        items.add(new DashboardItem(3, barChartInitializer, data));
        items.add(new DashboardItem(2, barChartInitializer, data));
        items.add(new DashboardItem(1, imageViewInitializer, R.mipmap.ic_launcher));

        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout.getRootView().setBackgroundColor(Color.parseColor("#EEEEEE"));


        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        int row = 0;
        int col = 0;
        for (DashboardItem item : items)
        {
            CardView card = (CardView) inflater.inflate(R.layout.card_item, null);
            View view = null;

            view = (View) item.initializer.construct(this, item.data);

            if (view == null)
                continue;

            card.addView(view);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.height = dp(150);
            int margin = dp(5);
            params.setMargins(margin, margin, margin, margin);
            GridLayout.Spec rowSpec = GridLayout.spec(row);
            GridLayout.Spec columnSpec = GridLayout.spec(col, item.columnSpan, 1);
            params.rowSpec = rowSpec;
            params.columnSpec = columnSpec;

            gridLayout.addView(card, params);

            col = (col + item.columnSpan) % gridLayout.getColumnCount();
            row = col == 0 ? row + 1 : row;
        }

//        barChart = (BarChart) findViewById(R.id.barChart);

//        barChart.getRootView().setBackgroundColor(Color.parseColor("#EEEEEE"));

//        List<BarEntry> entries = new ArrayList<>(25);
//        List<String> vals = new ArrayList<>(25);
//        Random random = new Random();
//        for (int i = 0; i < 25; ++i)
//        {
//            entries.add(new BarEntry(i, random.nextFloat()));
//            vals.add(String.valueOf(i));
//        }
//        BarDataSet dataSet = new BarDataSet(entries, "Steps");
//        dataSet.setColor(Color.parseColor("#43A047"));
////        BarData data = new BarData(vals, dataSet);
//        BarData data = new BarData(dataSet);
//        data.setBarWidth(0.2f);
//        barChart.setData(data);
//        barChart.fitScreen();
//        barChart.setDescription(null);
//        barChart.setDrawBorders(false);
//
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
//        xAxis.setDrawAxisLine(false);
//
//        YAxis leftYAxis = barChart.getAxisLeft();
//        leftYAxis.setDrawGridLines(false);
//        leftYAxis.setDrawLabels(false);
//        leftYAxis.setDrawAxisLine(false);
//
//        YAxis rightYAxis = barChart.getAxisRight();
//        rightYAxis.setDrawGridLines(false);
//        rightYAxis.setDrawLabels(false);
//        rightYAxis.setDrawAxisLine(false);

    }

    @Override
    public void onDataReceived(IJsonObject probeConfig, IJsonObject data)
    {
        int x = 5;
    }

    @Override
    public void onDataCompleted(IJsonObject probeConfig, JsonElement checkpoint)
    {
        updateScanCount();
        // Re-register to keep listening after probe completes.
//        wifiProbe.registerPassiveListener(this);
//        locationProbe.registerPassiveListener(this);
    }

    private void updateScanCount() {
        // Query the pipeline db for the count of rows in the data table
//        SQLiteDatabase db = pipeline.getDb();
//        Cursor cursor = db.rawQuery("select * from " + NameValueDatabaseHelper.DATA_TABLE.name, null);
//        while (cursor.moveToNext())
//        {
//            String id = cursor.getString(0);
//            String device = cursor.getString(1);
//            long timestamp = cursor.getLong(2);
//            String value = cursor.getString(3);
//
//            int x = 5;
//        }
    }
}
