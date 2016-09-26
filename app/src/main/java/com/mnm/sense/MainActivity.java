package com.mnm.sense;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Utils;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements Probe.DataListener
{
    public static final String PIPELINE_NAME = "default";
    private static final String TOTAL_COUNT_SQL = "SELECT count(*) FROM " + NameValueDatabaseHelper.DATA_TABLE.name;
    private FunfManager funfManager;
    private BasicPipeline pipeline;
    private WifiProbe wifiProbe;
    private BatteryProbe locationProbe;
    private CheckBox enabledCheckbox;
    private Button archiveButton, scanNowButton;
    private TextView dataCountView;
    private Handler handler;
    private ServiceConnection funfManagerConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            funfManager = ((FunfManager.LocalBinder)service).getManager();

            Gson gson = funfManager.getGson();
            wifiProbe = gson.fromJson(new JsonObject(), WifiProbe.class);
            locationProbe = gson.fromJson(new JsonObject(), BatteryProbe.class);
            pipeline = (BasicPipeline) funfManager.getRegisteredPipeline(PIPELINE_NAME);
//            pipeline.getDb().delete(NameValueDatabaseHelper.DATA_TABLE.name, null, null);
//            wifiProbe.registerPassiveListener(MainActivity.this);
            locationProbe.registerPassiveListener(MainActivity.this);

            // This checkbox enables or disables the pipeline
            enabledCheckbox.setChecked(pipeline.isEnabled());
            enabledCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (funfManager != null) {
                        if (isChecked) {
                            funfManager.enablePipeline(PIPELINE_NAME);
                            pipeline = (BasicPipeline) funfManager.getRegisteredPipeline(PIPELINE_NAME);
                        } else {
                            funfManager.disablePipeline(PIPELINE_NAME);
                        }
                    }
                }
            });

            // Set UI ready to use, by enabling buttons
            enabledCheckbox.setEnabled(true);
            archiveButton.setEnabled(true);
            scanNowButton.setEnabled(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            funfManager = null;
        }
    };

    SensingKitLibInterface mSensingKitLib;
    int stepCounter = 0;
    TextView activityText;
    TextView detectorText;
    TextView stepCounterText;
    TextView humidityText;
    TextView temperatureText;

    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        activityText = (TextView) findViewById(R.id.activityText);
        detectorText = (TextView) findViewById(R.id.detectorText);
        stepCounterText = (TextView) findViewById(R.id.stepCounterText);
        humidityText = (TextView) findViewById(R.id.humidityText);
        temperatureText = (TextView) findViewById(R.id.temperatureText);
        barChart = (BarChart) findViewById(R.id.barChart);

        barChart.getRootView().setBackgroundColor(Color.parseColor("#EEEEEE"));

        List<BarEntry> entries = new ArrayList<>(25);
        List<String> vals = new ArrayList<>(25);
        Random random = new Random();
        for (int i = 0; i < 25; ++i)
        {
            entries.add(new BarEntry(i, random.nextFloat()));
            vals.add(String.valueOf(i));
        }
        BarDataSet dataSet = new BarDataSet(entries, "Steps");
        dataSet.setColor(Color.parseColor("#43A047"));
//        BarData data = new BarData(vals, dataSet);
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.2f);
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

        try
        {
            mSensingKitLib = SensingKitLib.getSensingKitLib(this);
            mSensingKitLib.registerSensor(SKSensorType.STEP_DETECTOR);
            mSensingKitLib.registerSensor(SKSensorType.STEP_COUNTER);
            mSensingKitLib.registerSensor(SKSensorType.MOTION_ACTIVITY);

            mSensingKitLib.subscribeSensorDataListener(SKSensorType.STEP_DETECTOR, new SKSensorDataListener()
            {
                @Override
                public void onDataReceived(SKSensorType skSensorModuleType, SKSensorData skSensorData)
                {
                    SKStepDetectorData data = (SKStepDetectorData) skSensorData;
//                    Toast.makeText(MainActivity.this, "Step detected", Toast.LENGTH_SHORT).show();
                    ++stepCounter;
                    detectorText.setText("Detected steps: " + String.valueOf(stepCounter));
                }
            });

            mSensingKitLib.subscribeSensorDataListener(SKSensorType.STEP_COUNTER, new SKSensorDataListener()
            {
                @Override
                public void onDataReceived(SKSensorType skSensorModuleType, SKSensorData skSensorData)
                {
                    SKStepCounterData data = (SKStepCounterData) skSensorData;
//                    Toast.makeText(MainActivity.this, String.valueOf(data.getSteps()), Toast.LENGTH_SHORT).show();
                    stepCounterText.setText("Steps counted: " + String.valueOf(data.getSteps()));
                }
            });

            mSensingKitLib.subscribeSensorDataListener(SKSensorType.MOTION_ACTIVITY, new SKSensorDataListener()
            {
                @Override
                public void onDataReceived(SKSensorType skSensorType, SKSensorData skSensorData)
                {
                    SKMotionActivityData data = (SKMotionActivityData) skSensorData;
                    activityText.setText(data.getActivityString() + " : " + String.valueOf(data.getConfidence()));
                }
            });

            mSensingKitLib.startContinuousSensingWithAllRegisteredSensors();
        }
        catch (SKException e)
        {
            e.printStackTrace();
        }

        // Displays the count of rows in the data
        dataCountView = (TextView) findViewById(R.id.dataCountText);

        // Used to make interface changes on main thread
        handler = new Handler();

        enabledCheckbox = (CheckBox) findViewById(R.id.enabledCheckbox);
        enabledCheckbox.setEnabled(false);

        // Runs an archive if pipeline is enabled
        archiveButton = (Button) findViewById(R.id.archiveButton);
        archiveButton.setEnabled(false);
        archiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pipeline.isEnabled()) {
                    pipeline.onRun(BasicPipeline.ACTION_ARCHIVE, null);

                    // Wait 1 second for archive to finish, then refresh the UI
                    // (Note: this is kind of a hack since archiving is seamless and there are no messages when it occurs)
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Archived!", Toast.LENGTH_SHORT).show();
                            updateScanCount();
                        }
                    }, 1000L);
                } else {
                    Toast.makeText(getBaseContext(), "Pipeline is not enabled.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Bind to the service, to create the connection with FunfManager
        bindService(new Intent(this, FunfManager.class), funfManagerConn, BIND_AUTO_CREATE);

        scanNowButton = (Button) findViewById(R.id.scanNowButton);
        scanNowButton.setEnabled(false);
        scanNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pipeline.isEnabled()) {
                    // Manually register the pipeline
//                    wifiProbe.registerListener(pipeline);
                    locationProbe.registerListener(pipeline);
                } else {
                    Toast.makeText(getBaseContext(), "Pipeline is not enabled.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        locationProbe.registerPassiveListener(this);
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
