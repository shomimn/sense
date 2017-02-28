package com.mnm.sense.map;

import java.util.ArrayList;

public class OverlayManager
{
    ArrayList<SenseOverlay> overlays;

    public OverlayManager()
    {
        overlays = new ArrayList<>();
    }

    public void add(SenseOverlay overlay)
    {
        overlays.add(overlay);
    }

    public ArrayList<SenseOverlay> overlays()
    {
        return overlays;
    }
}
