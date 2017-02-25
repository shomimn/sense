package com.mnm.sense;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.TreeMap;

import static android.content.Context.LOCATION_SERVICE;

public class Locator implements LocationListener
{
    public static final long ACCURACY_THRESHOLD = 10000;
    public static final long CLEANUP_THRESHOLD = 3 * 60 *  60000;

    private static Locator instance_ = new Locator();

    private LocationManager locationManager = (LocationManager) SenseApp.context().getSystemService(LOCATION_SERVICE);
    private TreeMap<Long, Location> locationMap;
    private long lastCleanup;
    private boolean waitingForLocation;

    private Locator()
    {
        locationMap = new TreeMap<>();
        lastCleanup = System.currentTimeMillis();
        waitingForLocation = false;
    }

    public static synchronized Locator instance()
    {
        return instance_;
    }

    public synchronized Location locateAt(long timestamp)
    {
        Long match = bestMatch(timestamp);

        if (match != null)
            return locationMap.get(match);

        return null;
    }

    public synchronized Location lastLocation()
    {
        long now = System.currentTimeMillis();
        long match = locationMap.floorKey(now);

        return locationMap.get(match);
    }

    public synchronized void acquireNewLocation()
    {
        waitingForLocation = true;
    }

    private synchronized Long bestMatch(long timestamp)
    {
        Long prev = locationMap.floorKey(timestamp);
        Long next = locationMap.ceilingKey(timestamp);

        if (prev == null)
            return next;

        if (next == null)
            return prev;

        long prevDiff = timestamp - prev;
        long nextDiff = next - timestamp;

        if (prevDiff < nextDiff && prevDiff <= ACCURACY_THRESHOLD)
            return prev;

        if (nextDiff < prevDiff && nextDiff <= ACCURACY_THRESHOLD)
            return next;

        return null;
    }

    private synchronized void cleanup()
    {
        locationMap.clear();
        lastCleanup = System.currentTimeMillis();
    }

    public synchronized void requestLocationUpdates()
    {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        String provider = locationManager.getBestProvider(criteria, true);
        try
        {
            locationManager.requestLocationUpdates(provider, 5000, 0, this);
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
    }

    public synchronized void stopLocationUpdates()
    {
        try
        {
            locationManager.removeUpdates(this);
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onLocationChanged(Location location)
    {
        Log.d("Locator", location.toString());

        long now = System.currentTimeMillis();

        if (now - lastCleanup >= CLEANUP_THRESHOLD)
            cleanup();

        locationMap.put(now, location);

        notifyAll();
    }

    @Override
    public synchronized  void onStatusChanged(String s, int i, Bundle bundle)
    {

    }

    @Override
    public synchronized  void onProviderEnabled(String s)
    {

    }

    @Override
    public synchronized void onProviderDisabled(String s)
    {

    }
}
