package com.mnm.sense.map;

import java.util.ArrayList;

public class LineManager
{
    private ArrayList<AttributedFeature> lines;

    public LineManager()
    {
        lines = new ArrayList<>();
    }

    public void add(AttributedFeature line)
    {
        lines.add(line);
    }

    public ArrayList<AttributedFeature> getLines()
    {
        return lines;
    }
}
