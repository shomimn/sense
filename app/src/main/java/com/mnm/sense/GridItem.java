package com.mnm.sense;


public class GridItem
{
    public int rowSpan;
    public int columnSpan;
    public Object data;
    public boolean priority;

    public GridItem(int rows, int cols, Object d)
    {
        rowSpan = rows;
        columnSpan = cols;
        data = d;
        priority = false;
    }

    public GridItem(int rows, int cols, Object d, boolean p)
    {
        rowSpan = rows;
        columnSpan = cols;
        data = d;
        priority = p;
    }
}
